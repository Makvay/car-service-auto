import { useCallback, useEffect, useState } from "react";
import api from "../services/api";
import { normalizeList } from "../utils/normalize";
import HelpPanel from "../components/HelpPanel";

export default function NSI() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [items, setItems] = useState([]);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const res = await api.get("/api/nsi/");
      if (Array.isArray(res.data)) {
        setItems(res.data);
      } else {
        const flattened = Object.entries(res.data ?? {}).flatMap(([section, values]) =>
          normalizeList(values).map((value) => ({ ...value, _section: section }))
        );
        setItems(flattened);
      }
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
            <div className="text-sm text-black/60">Справочники: GET `/api/nsi/`</div>
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
          <div className="p-5 text-sm text-black/60">Загрузка...</div>
        ) : items.length === 0 ? (
          <div className="p-5 text-sm text-black/60">Нет данных</div>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-black/[0.03] text-black/70">
              <tr>
                <th className="text-left font-semibold px-4 py-3">Раздел</th>
                <th className="text-left font-semibold px-4 py-3">Ключ</th>
                <th className="text-left font-semibold px-4 py-3">Значение</th>
              </tr>
            </thead>
            <tbody>
              {items.slice(0, 50).map((item, idx) => (
                // eslint-disable-next-line react/no-array-index-key
                <tr key={item?.id ?? item?.key ?? idx}>
                  <td className="px-4 py-3 border-t border-black/10">
                    {item?._section ?? "common"}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10 tabular-nums">
                    {item?.key ?? item?.code ?? item?.id ?? "—"}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10">
                    {item?.value ?? item?.name ?? item?.title ?? JSON.stringify(item)}
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
