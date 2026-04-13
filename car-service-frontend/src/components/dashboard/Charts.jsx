import { useCallback, useEffect, useMemo, useState } from "react";
import {
  BarChart,
  Bar,
  CartesianGrid,
  Cell,
  Legend,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis
} from "recharts";
import api from "../../services/api";
import { normalizeList } from "../../utils/normalize";

const pieColors = ["#FF6B00", "#111111", "#6B7280", "#9CA3AF", "#D1D5DB"];

export default function Charts() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [claims, setClaims] = useState([]);
  const [supplies, setSupplies] = useState([]);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const [claimsRes, suppliesRes] = await Promise.all([
        api.get("/api/v1/claim/"),
        api.get("/api/v1/supplies/")
      ]);
      setClaims(normalizeList(claimsRes.data));
      setSupplies(normalizeList(suppliesRes.data));
    } catch (e) {
      setError(e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const claimsByStatus = useMemo(() => {
    const map = new Map();
    for (const c of claims) {
      const s = String(c?.status ?? "UNKNOWN").toUpperCase();
      map.set(s, (map.get(s) ?? 0) + 1);
    }
    return Array.from(map.entries())
      .map(([name, value]) => ({ name, value }))
      .sort((a, b) => b.value - a.value);
  }, [claims]);

  const warehouseTurnover = useMemo(() => {
    // Best-effort "оборот": суммируем amount/total/price*quantity по поставкам.
    const rows = supplies.map((s) => {
      const createdAt = s?.createdAt ?? s?.date ?? s?.supplyDate ?? null;
      const label = createdAt ? String(createdAt).slice(0, 10) : "—";
      const qty = Number(s?.quantity ?? s?.qty);
      const unitPrice = Number(s?.price ?? s?.unitPrice);
      const total = Number(s?.total ?? s?.amount);

      let turnover = 0;
      if (Number.isFinite(total)) turnover = total;
      else if (Number.isFinite(qty) && Number.isFinite(unitPrice))
        turnover = qty * unitPrice;
      else turnover = 1; // fallback: count of supply records

      return { date: label, turnover };
    });

    const byDate = new Map();
    for (const r of rows) {
      byDate.set(r.date, (byDate.get(r.date) ?? 0) + r.turnover);
    }

    return Array.from(byDate.entries())
      .map(([date, turnover]) => ({
        date,
        turnover: Math.round(turnover * 100) / 100
      }))
      .slice(-14);
  }, [supplies]);

  if (error) {
    return (
      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <div className="font-semibold">Графики</div>
        <div className="mt-2 text-sm text-black/70">
          Ошибка загрузки: {String(error?.message ?? error)}
        </div>
        <button
          type="button"
          onClick={load}
          className="mt-4 rounded-xl bg-black text-white px-4 py-2 text-sm font-semibold hover:bg-black/90"
        >
          Повторить
        </button>
      </div>
    );
  }

  return (
    <section className="mt-6">
      <div className="flex items-center justify-between">
        <div>
          <div className="text-lg font-semibold">Аналитика</div>
          <div className="text-sm text-black/60">
            Статусы заявок и оборот склада (best-effort по данным API).
          </div>
        </div>
        <button
          type="button"
          onClick={load}
          className="rounded-xl border border-black/15 bg-white px-4 py-2 text-sm font-semibold hover:bg-black/5"
        >
          Обновить
        </button>
      </div>

      <div className="mt-4 grid grid-cols-1 xl:grid-cols-2 gap-4">
        <div className="rounded-2xl border border-black/10 bg-white p-5">
          <div className="font-semibold">Заявки по статусам</div>
          <div className="mt-4 h-72">
            {loading ? (
              <div className="text-sm text-black/60">Загрузка…</div>
            ) : claimsByStatus.length === 0 ? (
              <div className="text-sm text-black/60">Нет данных</div>
            ) : (
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Tooltip />
                  <Legend />
                  <Pie
                    data={claimsByStatus}
                    dataKey="value"
                    nameKey="name"
                    innerRadius={55}
                    outerRadius={85}
                    paddingAngle={2}
                    isAnimationActive={false}
                  >
                    {claimsByStatus.map((_, idx) => (
                      <Cell
                        // eslint-disable-next-line react/no-array-index-key
                        key={idx}
                        fill={pieColors[idx % pieColors.length]}
                      />
                    ))}
                  </Pie>
                </PieChart>
              </ResponsiveContainer>
            )}
          </div>
          {!loading && claimsByStatus.length > 0 ? (
            <div className="mt-3 flex flex-wrap gap-2">
              {claimsByStatus.slice(0, 8).map((x, idx) => (
                <span
                  key={x.name}
                  className="inline-flex items-center gap-2 rounded-full border border-black/10 bg-white px-3 py-1 text-xs"
                >
                  <span
                    className="h-2 w-2 rounded-full"
                    style={{ background: pieColors[idx % pieColors.length] }}
                  />
                  <span className="font-medium">{x.name}</span>
                  <span className="text-black/60 tabular-nums">{x.value}</span>
                </span>
              ))}
            </div>
          ) : null}
        </div>

        <div className="rounded-2xl border border-black/10 bg-white p-5">
          <div className="font-semibold">Оборот склада (поставки)</div>
          <div className="mt-4 h-72">
            {loading ? (
              <div className="text-sm text-black/60">Загрузка…</div>
            ) : warehouseTurnover.length === 0 ? (
              <div className="text-sm text-black/60">Нет данных</div>
            ) : (
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={warehouseTurnover}>
                  <CartesianGrid strokeDasharray="3 3" stroke="rgba(17,17,17,0.12)" />
                  <XAxis dataKey="date" tick={{ fontSize: 12 }} />
                  <YAxis tick={{ fontSize: 12 }} />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="turnover" name="Оборот" fill="#FF6B00" radius={[8, 8, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            )}
          </div>
        </div>
      </div>
    </section>
  );
}

