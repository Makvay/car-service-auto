import { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import api from "../services/api";
import { normalizeList } from "../utils/normalize";
import HelpPanel from "../components/HelpPanel";

export default function Warehouse() {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [parts, setParts] = useState([]);
  const [claims, setClaims] = useState([]);

  const [reserving, setReserving] = useState(false);
  const [reserveError, setReserveError] = useState(null);
  const [reserveForm, setReserveForm] = useState({
    claimId: "",
    partId: "",
    quantity: "1"
  });

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const [partsRes, claimsRes] = await Promise.all([
        api.get("/api/v1/parts"),
        api.get("/api/v1/claim")
      ]);
      setParts(normalizeList(partsRes.data));
      setClaims(normalizeList(claimsRes.data) || []);
    } catch (e) {
      setError(e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const onReserveChange = useCallback((e) => {
    const { name, value } = e.target;
    setReserveForm((p) => ({ ...p, [name]: value }));
  }, []);

  const canReserve = useMemo(() => {
    return reserveForm.claimId.trim().length > 0 && 
           reserveForm.partId.trim().length > 0 && 
           Number(reserveForm.quantity) > 0;
  }, [reserveForm]);

  const createReserve = useCallback(
    async (e) => {
      e.preventDefault();
      if (!canReserve) return;

      setReserving(true);
      setReserveError(null);
      try {
        await api.post("/api/v1/reservations", {
          claimId: Number(reserveForm.claimId),
          partId: Number(reserveForm.partId),
          quantity: Number(reserveForm.quantity)
        });
        setReserveForm({ claimId: "", partId: "", quantity: "1" });
        await load();
      } catch (err) {
        setReserveError(err);
      } finally {
        setReserving(false);
      }
    },
    [canReserve, reserveForm, load]
  );

  return (
    <div className="space-y-6">
      <HelpPanel />
      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <h2 className="text-lg font-semibold mb-4">Управление запчастями, остатками и резервами</h2>
        <button
          onClick={load}
          className="rounded-xl bg-black text-white px-4 py-2 text-sm font-semibold hover:bg-black/90"
        >
          Обновить
        </button>
      </div>

      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <h3 className="text-base font-semibold mb-3">Зарезервировать запчасть</h3>
        <form onSubmit={createReserve} className="flex flex-wrap gap-3 items-end">
          <div>
            <label className="block text-sm font-medium text-black/70 mb-1">Заявка *</label>
            <select
              name="claimId"
              value={reserveForm.claimId}
              onChange={onReserveChange}
              className="rounded-xl border border-black/15 bg-white px-3 py-2 text-sm w-56"
            >
              <option value="">Выберите заявку</option>
              {claims.map(c => (
                <option key={c.id} value={c.id}>
                  {c.claimNumber || `Заявка #${c.id}`}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-black/70 mb-1">ID запчасти *</label>
            <select
              name="partId"
              value={reserveForm.partId}
              onChange={onReserveChange}
              className="rounded-xl border border-black/15 bg-white px-3 py-2 text-sm w-40"
            >
              <option value="">Выберите запчасть</option>
              {parts.map(p => (
                <option key={p.id} value={p.id}>{p.sku} - {p.name}</option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-black/70 mb-1">Количество *</label>
            <input
              type="text"
              name="quantity"
              value={reserveForm.quantity}
              onChange={onReserveChange}
              className="rounded-xl border border-black/15 bg-white px-3 py-2 text-sm w-20"
            />
          </div>
          <button
            type="submit"
            disabled={!canReserve || reserving}
            className="rounded-xl bg-orange text-white px-4 py-2 text-sm font-semibold disabled:opacity-50"
          >
            {reserving ? "Сохранение..." : "Зарезервировать"}
          </button>
        </form>
        {reserveError ? (
          <div className="mt-3 text-sm text-red-600">
            Ошибка: {reserveError?.response?.data?.message || reserveError?.message || "Unknown error"}
          </div>
        ) : null}
      </div>

      <div className="rounded-2xl border border-black/10 bg-white overflow-hidden">
        <div className="px-5 py-3 bg-black/5 font-semibold text-sm">Запчасти</div>
        {loading ? (
          <div className="p-5 text-sm text-black/60">Загрузка...</div>
        ) : error ? (
          <div className="p-5 text-sm text-red-600">Ошибка: {error?.message}</div>
        ) : parts.length === 0 ? (
          <div className="p-5 text-sm text-black/60">Нет данных</div>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-black/[0.03] text-black/70">
              <tr>
                <th className="text-left font-semibold px-4 py-2">ID</th>
                <th className="text-left font-semibold px-4 py-2">Артикул</th>
                <th className="text-left font-semibold px-4 py-2">Название</th>
                <th className="text-left font-semibold px-4 py-2">Бренд</th>
                <th className="text-right font-semibold px-4 py-2">Цена</th>
              </tr>
            </thead>
            <tbody>
              {parts.map((p) => (
                <tr key={p.id} className="border-t border-black/10">
                  <td className="px-4 py-2">{p.id}</td>
                  <td className="px-4 py-2">{p.sku}</td>
                  <td className="px-4 py-2">{p.name}</td>
                  <td className="px-4 py-2">{p.brand}</td>
                  <td className="px-4 py-2 text-right">{p.unitPrice} руб</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}