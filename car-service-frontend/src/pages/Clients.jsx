import { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import api from "../services/api";
import { normalizeList } from "../utils/normalize";
import HelpPanel from "../components/HelpPanel";

function splitFullName(name = "") {
  const trimmed = name.trim();
  if (!trimmed) return ["", ""];

  const parts = trimmed.split(/\s+/);
  return [parts[0] ?? "", parts.slice(1).join(" ")];
}

const emptyForm = {
  id: "",
  firstName: "",
  lastName: "",
  phone: "",
  email: "",
  address: ""
};

export default function Clients() {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [items, setItems] = useState([]);

  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState(null);
  const [form, setForm] = useState(emptyForm);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get("/api/v1/clients/");
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

  const onChange = useCallback((e) => {
    const { name, value } = e.target;
    setForm((p) => ({ ...p, [name]: value }));
  }, []);

  const canSave = useMemo(() => 
    form.firstName.trim().length > 0 && form.lastName.trim().length > 0,
    [form.firstName, form.lastName]
  );

  const startEdit = useCallback((c) => {
    const [firstName, lastName] = splitFullName(c?.name ?? "");
    setForm({
      id: c?.id ?? "",
      firstName,
      lastName,
      phone: c?.phone ?? "",
      email: c?.email ?? "",
      address: c?.address ?? ""
    });
    setSaveError(null);
  }, []);

  const reset = useCallback(() => {
    setForm(emptyForm);
    setSaveError(null);
  }, []);

  const save = useCallback(
    async (e) => {
      e.preventDefault();
      if (!canSave) return;
      setSaving(true);
      setSaveError(null);
      try {
        const payload = {
          name: `${form.firstName.trim()} ${form.lastName.trim()}`.trim(),
          phone: form.phone.trim(),
          email: form.email.trim()
        };

        if (form.id) {
          await api.put("/api/v1/clients/" + form.id, payload);
        } else {
          await api.post("/api/v1/clients/", payload);
        }

        reset();
        await load();
      } catch (err) {
        setSaveError(err);
      } finally {
        setSaving(false);
      }
    },
    [canSave, form, load, reset]
  );

  const remove = useCallback(
    async (id) => {
      const ok = window.confirm(`Удалить клиента #${id}?`);
      if (!ok) return;
      try {
          await api.delete("/api/v1/clients/" + id);
        await load();
      } catch (err) {
        alert(`Ошибка удаления: ${err?.response?.data?.message || err?.message}`);
      }
    },
    [load]
  );

  return (
    <div className="space-y-6">
      <HelpPanel />
      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <div className="flex items-center justify-between">
          <div>
            <div className="text-lg font-semibold">{t("clients")}</div>
            <div className="text-sm text-black/60">
              Управление клиентами автосервиса
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

        <form onSubmit={save} className="mt-5 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div>
            <label className="block text-sm font-medium text-black/70 mb-1">
              Имя *
            </label>
            <input
              name="firstName"
              value={form.firstName}
              onChange={onChange}
              placeholder="Иван"
              required
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-black/70 mb-1">
              Фамилия *
            </label>
            <input
              name="lastName"
              value={form.lastName}
              onChange={onChange}
              placeholder="Петров"
              required
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-black/70 mb-1">
              Телефон
            </label>
            <input
              name="phone"
              value={form.phone}
              onChange={onChange}
              placeholder="+7 999 123-45-67"
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-black/70 mb-1">
              Email
            </label>
            <input
              name="email"
              type="email"
              value={form.email}
              onChange={onChange}
              placeholder="client@example.ru"
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-black/70 mb-1">
              Адрес
            </label>
            <input
              name="address"
              value={form.address}
              onChange={onChange}
              placeholder="г. Москва, ул. Примерная д.1"
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            />
          </div>
          <div className="flex items-end gap-3">
            <button
              type="submit"
              disabled={!canSave || saving}
              className="flex-1 rounded-xl bg-black text-white px-4 py-2 text-sm font-semibold disabled:opacity-50 hover:bg-black/90"
            >
              {saving ? "Сохранение..." : form.id ? "Обновить" : t("create")}
            </button>
            <button
              type="button"
              onClick={reset}
              className="rounded-xl border border-black/15 bg-white px-4 py-2 text-sm font-semibold hover:bg-black/5"
            >
              {t("cancel")}
            </button>
          </div>
        </form>

        {saveError ? (
          <div className="mt-3 text-sm text-red-600">
            Ошибка: {saveError?.response?.data?.message || saveError?.message}
          </div>
        ) : null}
      </div>

      <div className="rounded-2xl border border-black/10 bg-white overflow-hidden">
        {error ? (
          <div className="p-5 text-sm text-red-600">
            Ошибка: {error?.response?.data?.message || error?.message}
          </div>
        ) : loading ? (
          <div className="p-5 text-sm text-black/60">{t("loading")}</div>
        ) : items.length === 0 ? (
          <div className="p-5 text-sm text-black/60">{t("noData")}</div>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-black/[0.03] text-black/70">
              <tr>
                <th className="text-left font-semibold px-4 py-3">ID</th>
                <th className="text-left font-semibold px-4 py-3">ФИО</th>
                <th className="text-left font-semibold px-4 py-3">Телефон</th>
                <th className="text-left font-semibold px-4 py-3">Email</th>
                <th className="text-left font-semibold px-4 py-3">Адрес</th>
                <th className="text-right font-semibold px-4 py-3">Действия</th>
              </tr>
            </thead>
            <tbody>
              {items.map((c) => (
                <tr key={c?.id}>
                  <td className="px-4 py-3 border-t border-black/10 tabular-nums">{c?.id}</td>
                  <td className="px-4 py-3 border-t border-black/10">{c?.name || "—"}</td>
                  <td className="px-4 py-3 border-t border-black/10">{c?.phone || "—"}</td>
                  <td className="px-4 py-3 border-t border-black/10">{c?.email || "—"}</td>
                  <td className="px-4 py-3 border-t border-black/10">{c?.address || "—"}</td>
                  <td className="px-4 py-3 border-t border-black/10 text-right">
                    <div className="flex justify-end gap-2">
                      <button
                        type="button"
                        onClick={() => startEdit(c)}
                        className="rounded-xl border border-black/15 bg-white px-3 py-2 text-sm font-semibold hover:bg-black/5"
                      >
                        {t("edit")}
                      </button>
                      <button
                        type="button"
                        onClick={() => remove(c.id)}
                        className="rounded-xl border border-red-300 bg-white px-3 py-2 text-sm font-semibold text-red-600 hover:bg-red-50"
                      >
                        {t("delete")}
                      </button>
                    </div>
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
