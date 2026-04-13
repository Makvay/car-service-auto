import { useCallback, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import api from "../services/api";
import { normalizeList } from "../utils/normalize";
import HelpPanel from "../components/HelpPanel";

export default function NSI() {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [items, setItems] = useState([]);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get("/api/nsi/");
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

  return (
    <div className="space-y-6">
      <HelpPanel />
      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <div className="flex items-center justify-between">
          <div>
            <div className="text-lg font-semibold">NSI</div>
            <div className="text-sm text-black/60">Справочники: GET ` /api/nsi/ `</div>
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
          <div className="p-5 text-sm text-black/60">Нет данных</div>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-black/[0.03] text-black/70">
              <tr>
                <th className="text-left font-semibold px-4 py-3">Ключ</th>
                <th className="text-left font-semibold px-4 py-3">Значение</th>
              </tr>
            </thead>
            <tbody>
              {items.slice(0, 50).map((x, idx) => (
                // eslint-disable-next-line react/no-array-index-key
                <tr key={x?.id ?? x?.key ?? idx}>
                  <td className="px-4 py-3 border-t border-black/10 tabular-nums">
                    {x?.key ?? x?.code ?? x?.id ?? "—"}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10">
                    {x?.value ?? x?.name ?? x?.title ?? JSON.stringify(x)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

