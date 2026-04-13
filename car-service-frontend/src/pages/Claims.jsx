import { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import api from "../services/api";
import { normalizeList } from "../utils/normalize";
import HelpPanel from "../components/HelpPanel";

const STATUS_OPTIONS = [
  { value: "CREATED", label: "Новая" },
  { value: "IN_PROGRESS", label: "В работе" },
  { value: "WAITING_PARTS", label: "Ожидает запчасти" },
  { value: "COMPLETED", label: "Готова" },
  { value: "CANCELLED", label: "Отменена" }
];

export default function Claims() {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [items, setItems] = useState([]);

  const [clients, setClients] = useState([]);
  const [services, setServices] = useState([]);

  const [creating, setCreating] = useState(false);
  const [createError, setCreateError] = useState(null);
  const [form, setForm] = useState({
    clientId: "",
    vehicleId: "",
    serviceId: "",
    scheduledDate: "",
    description: ""
  });

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const [claimsRes, clientsRes, servicesRes] = await Promise.all([
        api.get("/api/v1/claim"),
        api.get("/api/v1/clients/"),
        api.get("/api/nsi/")
      ]);
      setItems(normalizeList(claimsRes.data));
      setClients(normalizeList(clientsRes.data) || []);
      setServices(normalizeList(servicesRes.data?.services) || []);
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

  const canCreate = useMemo(() => {
    return form.clientId && form.serviceId && form.description.trim().length > 0;
  }, [form.clientId, form.serviceId, form.description]);

  const create = useCallback(
    async (e) => {
      e.preventDefault();
      if (!canCreate) return;

      setCreating(true);
      setCreateError(null);
      try {
        await api.post("/api/v1/claim", {
          clientId: Number(form.clientId),
          vehicleId: form.vehicleId ? Number(form.vehicleId) : null,
          mileageAtEntry: 0,
          problemDescription: form.description.trim()
        });
        setForm({
          clientId: "",
          vehicleId: "",
          serviceId: "",
          scheduledDate: "",
          description: ""
        });
        await load();
      } catch (err) {
        setCreateError(err);
      } finally {
        setCreating(false);
      }
    },
    [canCreate, form, load]
  );

  const updateStatus = useCallback(
    async (id, status) => {
      try {
        await api.put(`/api/v1/claim/${id}/status`, { status });
        await load();
      } catch (err) {
        console.error("Failed to update status:", err);
      }
    },
    [load]
  );

  const getStatusLabel = (status) => {
    return STATUS_OPTIONS.find(s => s.value === status)?.label || status;
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "CREATED": return "bg-blue-100 text-blue-800";
      case "IN_PROGRESS": return "bg-yellow-100 text-yellow-800";
      case "WAITING_PARTS": return "bg-orange-100 text-orange-800";
      case "COMPLETED": return "bg-green-100 text-green-800";
      case "CANCELLED": return "bg-red-100 text-red-800";
      default: return "bg-gray-100 text-gray-800";
    }
  };

  return (
    <div className="space-y-6">
      <HelpPanel />
      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <h2 className="text-lg font-semibold mb-4">Создание и управление заявками на обслуживание</h2>
        <button
          onClick={load}
          className="rounded-xl bg-black text-white px-4 py-2 text-sm font-semibold hover:bg-black/90"
        >
          Обновить
        </button>
      </div>

      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <h3 className="text-base font-semibold mb-3">Создать новую заявку</h3>
        <form onSubmit={create} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-black/70 mb-1">Клиент *</label>
            <select
              name="clientId"
              value={form.clientId}
              onChange={onChange}
              required
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            >
              <option value="">Выберите клиента</option>
              {clients.map(c => (
                <option key={c.id} value={c.id}>#{c.id} - {c.name || c.firstName} {c.lastName || ''}</option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-black/70 mb-1">Услуга *</label>
            <select
              name="serviceId"
              value={form.serviceId}
              onChange={onChange}
              required
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            >
              <option value="">Выберите услугу</option>
              {services.map(s => (
                <option key={s.id} value={s.id}>{s.name}</option>
              ))}
            </select>
          </div>
          <div className="md:col-span-2">
            <label className="block text-sm font-medium text-black/70 mb-1">Описание проблемы *</label>
            <textarea
              name="description"
              value={form.description}
              onChange={onChange}
              required
              placeholder="Описание работы"
              rows={2}
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            />
          </div>
          <div className="md:col-span-2">
            <button
              type="submit"
              disabled={!canCreate || creating}
              className="rounded-xl bg-orange text-white px-6 py-2 text-sm font-semibold disabled:opacity-50"
            >
              {creating ? "Создание..." : "Создать заявку"}
            </button>
          </div>
        </form>
        {createError ? (
          <div className="mt-3 text-sm text-red-600">Ошибка: {createError?.message || createError}</div>
        ) : null}
      </div>

      <div className="rounded-2xl border border-black/10 bg-white overflow-hidden">
        {loading ? (
          <div className="p-5 text-sm text-black/60">Загрузка...</div>
        ) : error ? (
          <div className="p-5 text-sm text-red-600">Ошибка: {error?.message}</div>
        ) : items.length === 0 ? (
          <div className="p-5 text-sm text-black/60">Нет заявок</div>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-black/[0.03] text-black/70">
              <tr>
                <th className="text-left font-semibold px-4 py-3">№</th>
                <th className="text-left font-semibold px-4 py-3">Клиент</th>
                <th className="text-left font-semibold px-4 py-3">Статус</th>
                <th className="text-left font-semibold px-4 py-3">Описание</th>
                <th className="text-left font-semibold px-4 py-3">Приоритет</th>
                <th className="text-right font-semibold px-4 py-3">Действия</th>
              </tr>
            </thead>
            <tbody>
              {items.map((claim) => (
                <tr key={claim.id} className="border-t border-black/10">
                  <td className="px-4 py-3">{claim.claimNumber || claim.id}</td>
                  <td className="px-4 py-3">
                    {claim.clientFirstName ? `${claim.clientFirstName} ${claim.clientLastName}` : claim.clientId}
                  </td>
                  <td className="px-4 py-3">
                    <span className={`inline-block px-2 py-1 rounded text-xs font-medium ${getStatusColor(claim.status)}`}>
                      {getStatusLabel(claim.status)}
                    </span>
                  </td>
                  <td className="px-4 py-3">{claim.problemDescription || claim.description}</td>
                  <td className="px-4 py-3">{claim.priority || "—"}</td>
                  <td className="px-4 py-3 text-right">
                    <div className="flex justify-end gap-2">
                      <select
                        value={claim.status}
                        onChange={(e) => updateStatus(claim.id, e.target.value)}
                        className="rounded border border-black/15 bg-white px-2 py-1 text-xs"
                      >
                        {STATUS_OPTIONS.map(opt => (
                          <option key={opt.value} value={opt.value}>{opt.label}</option>
                        ))}
                      </select>
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