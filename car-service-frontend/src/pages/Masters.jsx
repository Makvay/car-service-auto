import { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import api from "../services/api";
import { normalizeList } from "../utils/normalize";
import HelpPanel from "../components/HelpPanel";

const emptyForm = {
  id: "",
  firstName: "",
  lastName: "",
  employeeCode: "",
  phone: "",
  email: "",
  specialization: "",
  qualificationLevel: "",
  hourlyRate: ""
};

const specializations = [
  { value: "DIAGNOSTICIAN", label: "Диагност" },
  { value: "ELECTRICIAN", label: "Электрик" },
  { value: "MOTOR_SPECIALIST", label: "Моторист" },
  { value: "BODY_REPAIRER", label: "Кузовщик" },
  { value: "PAINTER", label: "Маляр" },
  { value: "UNIVERSAL", label: "Универсал" }
];

const qualifications = [
  { value: "JUNIOR", label: "Младший" },
  { value: "MIDDLE", label: "Средний" },
  { value: "SENIOR", label: "Старший" },
  { value: "LEAD", label: "Ведущий" }
];

export default function Masters() {
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
      const res = await api.get("/api/v1/masters");
      const allItems = normalizeList(res.data);
      // Фильтруем только активных
      setItems(allItems.filter(m => m.isActive !== false));
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
    form.firstName.trim().length > 0 && 
    form.lastName.trim().length > 0 &&
    form.specialization.trim().length > 0,
    [form]
  );

  const startEdit = useCallback((m) => {
    setForm({
      id: m?.id ?? "",
      firstName: m?.firstName ?? "",
      lastName: m?.lastName ?? "",
      employeeCode: m?.employeeCode ?? "",
      phone: m?.phone ?? "",
      email: m?.email ?? "",
      specialization: m?.specialization ?? "",
      qualificationLevel: m?.qualificationLevel ?? "",
      hourlyRate: m?.hourlyRate ?? ""
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
        const phoneClean = form.phone.replace(/[\s\(\)\-]/g, '').trim();
        
        const payload = {
          firstName: form.firstName.trim(),
          lastName: form.lastName.trim(),
          employeeCode: form.employeeCode.trim(),
          phone: phoneClean || "+79990000000",
          email: form.email.trim() || "noemail@test.ru",
          specialization: form.specialization,
          qualificationLevel: form.qualificationLevel || null,
          hourlyRate: form.hourlyRate ? Number(form.hourlyRate) : 1000,
          hireDate: new Date().toISOString().split('T')[0]
        };

        if (form.id) {
          await api.put("/api/v1/masters/" + form.id, payload);
        } else {
          await api.post("/api/v1/masters", payload);
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
      const ok = window.confirm(`Удалить мастера #${id}?`);
      if (!ok) return;
      try {
        await api.delete("/api/v1/masters/" + id);
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
            <div className="text-lg font-semibold">{t("masters")}</div>
            <div className="text-sm text-black/60">
              CRUD: /api/v1/masters
            </div>
          </div>
          <button
            type="button"
            onClick={load}
            className="rounded-xl border border-black/15 bg-white px-4 py-2 text-sm font-semibold hover:bg-black/5"
          >
            {t("search").replace("...", "")}
          </button>
        </div>

        <form onSubmit={save} className="mt-5 space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
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
                Табельный номер
              </label>
              <input
                name="employeeCode"
                value={form.employeeCode}
                onChange={onChange}
                placeholder="M001"
                className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-black/70 mb-1">
                Телефон *
              </label>
              <input
                name="phone"
                value={form.phone}
                onChange={onChange}
                placeholder="+7 999 123-45-67"
                required
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
                placeholder="master@example.ru"
                className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-black/70 mb-1">
                Специализация *
              </label>
              <select
                name="specialization"
                value={form.specialization}
                onChange={onChange}
                required
                className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
              >
                <option value="">Выберите специализацию</option>
                {specializations.map((spec) => (
                  <option key={spec.value} value={spec.value}>
                    {spec.label}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-black/70 mb-1">
                Квалификация
              </label>
              <select
                name="qualificationLevel"
                value={form.qualificationLevel}
                onChange={onChange}
                className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
              >
                <option value="">Выберите</option>
                {qualifications.map((q) => (
                  <option key={q.value} value={q.value}>{q.label}</option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-black/70 mb-1">
                Ставка (руб/час)
              </label>
              <input
                name="hourlyRate"
                type="number"
                value={form.hourlyRate}
                onChange={onChange}
                placeholder="1500"
                min="0"
                step="100"
                className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
              />
            </div>
          </div>

          <div className="flex gap-3 pt-2">
            <button
              type="submit"
              disabled={!canSave || saving}
              className="rounded-xl bg-black text-white px-6 py-2 text-sm font-semibold disabled:opacity-50 hover:bg-black/90"
            >
              {saving ? "Сохранение..." : form.id ? "Обновить" : t("create")}
            </button>
            <button
              type="button"
              onClick={reset}
              className="rounded-xl border border-black/15 bg-white px-6 py-2 text-sm font-semibold hover:bg-black/5"
            >
              {t("cancel")}
            </button>
          </div>
        </form>

        {saveError ? (
          <div className="mt-3 text-sm text-red-600">
            Ошибка: {saveError?.response?.data?.message || saveError?.message || "Unknown error"}
          </div>
        ) : null}
      </div>

      <div className="rounded-2xl border border-black/10 bg-white overflow-hidden">
        {error ? (
          <div className="p-5 text-sm text-red-600">
            Ошибка загрузки: {error?.response?.data?.message || error?.message}
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
                <th className="text-left font-semibold px-4 py-3">Табельный</th>
                <th className="text-left font-semibold px-4 py-3">Специализация</th>
                <th className="text-left font-semibold px-4 py-3">Квалификация</th>
                <th className="text-left font-semibold px-4 py-3">Телефон</th>
                <th className="text-left font-semibold px-4 py-3">Ставка</th>
                <th className="text-right font-semibold px-4 py-3">Действия</th>
              </tr>
            </thead>
            <tbody>
              {items.map((m) => (
                <tr key={m?.id}>
                  <td className="px-4 py-3 border-t border-black/10 tabular-nums">
                    {m?.id}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10">
                    {m?.firstName} {m?.lastName}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10 tabular-nums">
                    {m?.employeeCode || "—"}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10">
                    {specializations.find(s => s.value === m?.specialization)?.label || m?.specialization || "—"}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10">
                    {qualifications.find(q => q.value === m?.qualificationLevel)?.label || m?.qualificationLevel || "—"}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10">{m?.phone || "—"}</td>
                  <td className="px-4 py-3 border-t border-black/10 tabular-nums">
                    {m?.hourlyRate ? `${m.hourlyRate} руб/ч` : "—"}
                  </td>
                  <td className="px-4 py-3 border-t border-black/10 text-right">
                    <div className="flex justify-end gap-2">
                      <button
                        type="button"
                        onClick={() => startEdit(m)}
                        className="rounded-xl border border-black/15 bg-white px-3 py-2 text-sm font-semibold hover:bg-black/5"
                      >
                        {t("edit")}
                      </button>
                      <button
                        type="button"
                        onClick={() => remove(m.id)}
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
