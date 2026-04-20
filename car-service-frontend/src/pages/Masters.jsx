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

function getSpecializationValue(master) {
  return master?.specialization ?? master?.specializations?.[0] ?? "";
}

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
      setItems(allItems.filter((master) => master.isActive !== false));
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
    setForm((prev) => ({ ...prev, [name]: value }));
  }, []);

  const canSave = useMemo(
    () =>
      form.employeeCode.trim().length > 0 &&
      form.firstName.trim().length > 0 &&
      form.lastName.trim().length > 0 &&
      form.specialization.trim().length > 0,
    [form.employeeCode, form.firstName, form.lastName, form.specialization]
  );

  const startEdit = useCallback((master) => {
    setForm({
      id: master?.id ?? "",
      firstName: master?.firstName ?? "",
      lastName: master?.lastName ?? "",
      employeeCode: master?.employeeCode ?? "",
      phone: master?.phone ?? "",
      email: master?.email ?? "",
      specialization: getSpecializationValue(master),
      qualificationLevel: master?.qualificationLevel ?? "",
      hourlyRate: master?.hourlyRate ?? ""
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
        const phoneClean = form.phone.replace(/[\s()-]/g, "").trim();

        const payload = {
          firstName: form.firstName.trim(),
          lastName: form.lastName.trim(),
          employeeCode: form.employeeCode.trim(),
          phone: phoneClean || "+79990000000",
          email: form.email.trim() || "noemail@test.ru",
          specializations: [form.specialization],
          qualificationLevel: form.qualificationLevel || null,
          hourlyRate: form.hourlyRate ? Number(form.hourlyRate) : 1000,
          hireDate: new Date().toISOString().split("T")[0]
        };

        if (form.id) {
          await api.put(`/api/v1/masters/${form.id}`, payload);
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
        await api.delete(`/api/v1/masters/${id}`);
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
            <div className="text-sm text-black/60">CRUD: /api/v1/masters</div>
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
          <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3">
            <div>
              <label className="mb-1 block text-sm font-medium text-black/70">Имя *</label>
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
              <label className="mb-1 block text-sm font-medium text-black/70">Фамилия *</label>
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
              <label className="mb-1 block text-sm font-medium text-black/70">Табельный номер *</label>
              <input
                name="employeeCode"
                value={form.employeeCode}
                onChange={onChange}
                placeholder="M001"
                required
                className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
              />
            </div>

            <div>
              <label className="mb-1 block text-sm font-medium text-black/70">Телефон *</label>
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
              <label className="mb-1 block text-sm font-medium text-black/70">Email</label>
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
              <label className="mb-1 block text-sm font-medium text-black/70">Специализация *</label>
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
              <label className="mb-1 block text-sm font-medium text-black/70">Квалификация</label>
              <select
                name="qualificationLevel"
                value={form.qualificationLevel}
                onChange={onChange}
                className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
              >
                <option value="">Выберите</option>
                {qualifications.map((qualification) => (
                  <option key={qualification.value} value={qualification.value}>
                    {qualification.label}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="mb-1 block text-sm font-medium text-black/70">Ставка (руб/час)</label>
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
              className="rounded-xl bg-black px-6 py-2 text-sm font-semibold text-white disabled:opacity-50 hover:bg-black/90"
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
                <th className="px-4 py-3 text-left font-semibold">ID</th>
                <th className="px-4 py-3 text-left font-semibold">ФИО</th>
                <th className="px-4 py-3 text-left font-semibold">Табельный</th>
                <th className="px-4 py-3 text-left font-semibold">Специализация</th>
                <th className="px-4 py-3 text-left font-semibold">Квалификация</th>
                <th className="px-4 py-3 text-left font-semibold">Телефон</th>
                <th className="px-4 py-3 text-left font-semibold">Ставка</th>
                <th className="px-4 py-3 text-right font-semibold">Действия</th>
              </tr>
            </thead>
            <tbody>
              {items.map((master) => {
                const specializationValue = getSpecializationValue(master);
                const specializationLabel =
                  specializations.find((item) => item.value === specializationValue)?.label ||
                  specializationValue ||
                  "—";
                const qualificationLabel =
                  qualifications.find((item) => item.value === master?.qualificationLevel)?.label ||
                  master?.qualificationLevel ||
                  "—";

                return (
                  <tr key={master?.id}>
                    <td className="border-t border-black/10 px-4 py-3 tabular-nums">{master?.id}</td>
                    <td className="border-t border-black/10 px-4 py-3">
                      {master?.firstName} {master?.lastName}
                    </td>
                    <td className="border-t border-black/10 px-4 py-3 tabular-nums">
                      {master?.employeeCode || "—"}
                    </td>
                    <td className="border-t border-black/10 px-4 py-3">{specializationLabel}</td>
                    <td className="border-t border-black/10 px-4 py-3">{qualificationLabel}</td>
                    <td className="border-t border-black/10 px-4 py-3">{master?.phone || "—"}</td>
                    <td className="border-t border-black/10 px-4 py-3 tabular-nums">
                      {master?.hourlyRate ? `${master.hourlyRate} руб/ч` : "—"}
                    </td>
                    <td className="border-t border-black/10 px-4 py-3 text-right">
                      <div className="flex justify-end gap-2">
                        <button
                          type="button"
                          onClick={() => startEdit(master)}
                          className="rounded-xl border border-black/15 bg-white px-3 py-2 text-sm font-semibold hover:bg-black/5"
                        >
                          {t("edit")}
                        </button>
                        <button
                          type="button"
                          onClick={() => remove(master.id)}
                          className="rounded-xl border border-red-300 bg-white px-3 py-2 text-sm font-semibold text-red-600 hover:bg-red-50"
                        >
                          {t("delete")}
                        </button>
                      </div>
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
