import { useCallback, useEffect, useMemo, useState } from "react";
import api from "../services/api";
import { normalizeList } from "../utils/normalize";
import HelpPanel from "../components/HelpPanel";

function isRead(notification) {
  const status = String(notification?.status ?? "").toUpperCase();
  return notification?.read === true || notification?.isRead === true || status === "READ";
}

export default function Notifications() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [items, setItems] = useState([]);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get("/api/v1/notification");
      setItems(normalizeList(res.data));
    } catch (e) {
      setError(e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const unreadCount = useMemo(() => {
    return items.filter((n) => !isRead(n)).length;
  }, [items]);

  const markRead = useCallback(
    async (id) => {
      await api.patch(`/api/v1/notification/${id}/read`);
      await load();
    },
    [load]
  );

  return (
    <div className="space-y-6">
      <HelpPanel />
      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <div className="flex items-center justify-between">
          <div>
            <div className="text-lg font-semibold">Notifications</div>
            <div className="text-sm text-black/60">
              Непрочитанных: <span className="font-semibold tabular-nums">{unreadCount}</span>
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
      </div>

      <div className="rounded-2xl border border-black/10 bg-white overflow-hidden">
        {error ? (
          <div className="p-5 text-sm text-black/70">
            Ошибка загрузки: {String(error?.message ?? error)}
          </div>
        ) : loading ? (
          <div className="p-5 text-sm text-black/60">Загрузка…</div>
        ) : items.length === 0 ? (
          <div className="p-5 text-sm text-black/60">Уведомлений нет</div>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-black/[0.03] text-black/70">
              <tr>
                <th className="text-left font-semibold px-4 py-3">ID</th>
                <th className="text-left font-semibold px-4 py-3">Сообщение</th>
                <th className="text-left font-semibold px-4 py-3">Дата</th>
                <th className="text-left font-semibold px-4 py-3">Статус</th>
                <th className="text-right font-semibold px-4 py-3">Действия</th>
              </tr>
            </thead>
            <tbody>
              {items.map((n) => {
                const read = isRead(n);
                return (
                  <tr key={n?.id ?? JSON.stringify(n)}>
                    <td className="px-4 py-3 border-t border-black/10 tabular-nums">
                      {n?.id ?? "—"}
                    </td>
                    <td className="px-4 py-3 border-t border-black/10">
                      <div className={read ? "text-black/60" : "font-semibold"}>
                        {n?.message ?? n?.text ?? n?.title ?? "—"}
                      </div>
                    </td>
                    <td className="px-4 py-3 border-t border-black/10 tabular-nums text-black/70">
                      {String(n?.createdAt ?? n?.date ?? "—").slice(0, 19)}
                    </td>
                    <td className="px-4 py-3 border-t border-black/10">
                      <span
                        className={[
                          "inline-flex items-center rounded-full border px-3 py-1 text-xs font-medium",
                          read ? "border-black/10 text-black/60" : "border-orange/40 text-orange"
                        ].join(" ")}
                      >
                        {read ? "READ" : "UNREAD"}
                      </span>
                    </td>
                    <td className="px-4 py-3 border-t border-black/10 text-right">
                      <button
                        type="button"
                        onClick={() => markRead(n.id)}
                        disabled={!n?.id || read}
                        className="rounded-xl bg-black text-white px-3 py-2 text-sm font-semibold disabled:opacity-50 hover:bg-black/90"
                      >
                        Прочитано
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

