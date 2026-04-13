import { useCallback, useEffect, useMemo, useState } from "react";
import api from "../../services/api";
import { normalizeList } from "../../utils/normalize";

function Card({ label, value, hint, loading }) {
  return (
    <div className="rounded-2xl border border-black/10 bg-white p-5">
      <div className="text-sm text-black/60">{label}</div>
      <div className="mt-2 text-3xl font-semibold tabular-nums">
        {loading ? "…" : value}
      </div>
      {hint ? <div className="mt-2 text-xs text-black/50">{hint}</div> : null}
    </div>
  );
}

export default function KPICards() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [claims, setClaims] = useState([]);
  const [masters, setMasters] = useState([]);
  const [inventory, setInventory] = useState([]);
  const [notifications, setNotifications] = useState([]);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const [claimsRes, mastersRes, inventoryRes, notifRes] = await Promise.all([
        api.get("/api/v1/claim/"),
        api.get("/api/v1/masters/"),
        api.get("/api/v1/inventory/"),
        api.get("/api/v1/notification/")
      ]);

      setClaims(normalizeList(claimsRes.data));
      setMasters(normalizeList(mastersRes.data));
      setInventory(normalizeList(inventoryRes.data));
      setNotifications(normalizeList(notifRes.data));
    } catch (e) {
      setError(e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const activeClaimsCount = useMemo(() => {
    // Если статус отсутствует — считаем все активными.
    return claims.filter((c) => {
      const s = String(c?.status ?? "").toUpperCase();
      if (!s) return true;
      return !["DONE", "COMPLETED", "CLOSED", "CANCELLED"].includes(s);
    }).length;
  }, [claims]);

  const freeMastersCount = useMemo(() => {
    // Best-effort: если есть available/isFree/busy — используем, иначе показываем общее число.
    const hasAvailability =
      masters.some((m) => typeof m?.available === "boolean") ||
      masters.some((m) => typeof m?.isFree === "boolean") ||
      masters.some((m) => typeof m?.busy === "boolean");

    if (!hasAvailability) return masters.length;

    return masters.filter((m) => {
      if (typeof m?.available === "boolean") return m.available;
      if (typeof m?.isFree === "boolean") return m.isFree;
      if (typeof m?.busy === "boolean") return !m.busy;
      return false;
    }).length;
  }, [masters]);

  const partsInStock = useMemo(() => {
    // inventory items usually: { partId, quantity } — если нет quantity, считаем строки.
    const quantities = inventory
      .map((x) => Number(x?.quantity))
      .filter((n) => Number.isFinite(n));
    if (quantities.length === 0) return inventory.length;
    return quantities.reduce((a, b) => a + b, 0);
  }, [inventory]);

  const unreadNotifications = useMemo(() => {
    return notifications.filter((n) => n?.read === false || n?.isRead === false)
      .length;
  }, [notifications]);

  if (error) {
    return (
      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <div className="font-semibold">KPI</div>
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
    <section>
      <div className="flex items-center justify-between">
        <div>
          <div className="text-lg font-semibold">Ключевые показатели</div>
          <div className="text-sm text-black/60">
            Данные агрегируются из микросервисов через Gateway.
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

      <div className="mt-4 grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-4">
        <Card
          label="Активные заявки"
          value={activeClaimsCount}
          hint="claim-service"
          loading={loading}
        />
        <Card
          label="Свободные мастера"
          value={freeMastersCount}
          hint="master-service"
          loading={loading}
        />
        <Card
          label="Запчастей на складе"
          value={partsInStock}
          hint="warehouse-service"
          loading={loading}
        />
        <Card
          label="Непрочитанные уведомления"
          value={unreadNotifications}
          hint="notification-service"
          loading={loading}
        />
      </div>
    </section>
  );
}

