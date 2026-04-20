import { useCallback, useEffect, useMemo, useState } from "react";
import api from "../../services/api";
import { normalizeList } from "../../utils/normalize";

export default function RecentClaims() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [claims, setClaims] = useState([]);
  const [clients, setClients] = useState([]);
  const [masters, setMasters] = useState([]);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const [claimsRes, clientsRes, mastersRes] = await Promise.all([
        api.get("/api/claims"),
        api.get("/api/v1/clients/"),
        api.get("/api/v1/masters"),
      ]);

      const loadedClients = normalizeList(clientsRes.data).map((client) => {
        if (client.firstName || client.lastName) return client;
        if (!client.name) return client;
        const parts = String(client.name).trim().split(/\s+/);
        return {
          ...client,
          firstName: parts[0] ?? "",
          lastName: parts.slice(1).join(" ") || "",
        };
      });

      setClaims(normalizeList(claimsRes.data));
      setClients(loadedClients);
      setMasters(normalizeList(mastersRes.data));
    } catch (e) {
      setError(e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const rows = useMemo(() => {
    const list = [...claims];
    list.sort((a, b) => {
      const da = new Date(a?.createdAt ?? a?.date ?? 0).getTime();
      const db = new Date(b?.createdAt ?? b?.date ?? 0).getTime();
      return db - da;
    });
    return list.slice(0, 5);
  }, [claims]);

  const getClientFullName = useCallback(
    (claim) => {
      if (claim?.clientFirstName || claim?.clientLastName) {
        return `${claim.clientFirstName ?? ""} ${claim.clientLastName ?? ""}`.trim();
      }
      const client = clients.find((item) => item.id === Number(claim?.clientId));
      if (!client) return "Клиент не найден";
      return `${client.firstName ?? ""} ${client.lastName ?? ""}`.trim() || client.name || "Клиент не указан";
    },
    [clients]
  );

  const getMasterFullName = useCallback(
    (claim) => {
      if (claim?.masterFirstName || claim?.masterLastName) {
        return `${claim.masterFirstName ?? ""} ${claim.masterLastName ?? ""}`.trim();
      }
      if (!claim?.masterId) return "Не назначен";
      const master = masters.find((item) => item.id === Number(claim.masterId));
      if (!master) return "Не назначен";
      return `${master.firstName ?? ""} ${master.lastName ?? ""}`.trim() || "Не назначен";
    },
    [masters]
  );

  return (
    <section className="mt-6">
      <div className="flex items-center justify-between">
        <div>
          <div className="text-lg font-semibold">Последние заявки</div>
          <div className="text-sm text-black/60">Топ-5 по дате создания</div>
        </div>
        <button
          type="button"
          onClick={load}
          className="rounded-xl border border-black/15 bg-white px-4 py-2 text-sm font-semibold hover:bg-black/5"
        >
          Обновить
        </button>
      </div>

      <div className="mt-4 rounded-2xl border border-black/10 bg-white overflow-hidden">
        {error ? (
          <div className="p-5 text-sm text-black/70">
            Ошибка загрузки: {String(error?.message ?? error)}
          </div>
        ) : loading ? (
          <div className="p-5 text-sm text-black/60">Загрузка...</div>
        ) : rows.length === 0 ? (
          <div className="p-5 text-sm text-black/60">Нет данных</div>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-black/[0.03] text-black/70">
              <tr>
                <th className="text-left font-semibold px-4 py-3">ID</th>
                <th className="text-left font-semibold px-4 py-3">Клиент</th>
                <th className="text-left font-semibold px-4 py-3">Мастер</th>
                <th className="text-left font-semibold px-4 py-3">Статус</th>
                <th className="text-left font-semibold px-4 py-3">Дата</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((claim) => (
                <tr key={claim?.id ?? `${claim?.clientId}-${claim?.masterId}-${claim?.problemDescription}`}>
                  <td className="px-4 py-3 border-t border-black/10 tabular-nums">
                    {claim?.id ?? "-"}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10">
                    {getClientFullName(claim)}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10">
                    {getMasterFullName(claim)}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10">
                    <span className="inline-flex items-center rounded-full border border-black/10 px-3 py-1 text-xs font-medium">
                      {String(claim?.status ?? "-")}
                    </span>
                  </td>
                  <td className="px-4 py-3 border-t border-black/10 tabular-nums text-black/70">
                    {String(claim?.createdAt ?? claim?.date ?? "-").slice(0, 19)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </section>
  );
}
