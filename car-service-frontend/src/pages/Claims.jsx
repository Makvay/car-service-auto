import { Fragment, useCallback, useEffect, useMemo, useState } from "react";
import api from "../services/api";
import { normalizeList } from "../utils/normalize";
import HelpPanel from "../components/HelpPanel";

const STATUS_OPTIONS = [
  { value: "CREATED", label: "Новая" },
  { value: "IN_PROGRESS", label: "В работе" },
  { value: "WAITING_PARTS", label: "Ожидает запчасти" },
  { value: "COMPLETED", label: "Готова" },
  { value: "CANCELLED", label: "Отменена" },
];

const PRIORITY_OPTIONS = [
  { value: "URGENT", label: "Срочный" },
  { value: "HIGH", label: "Высокий" },
  { value: "NORMAL", label: "Обычный" },
  { value: "LOW", label: "Низкий" },
];

const EMPTY_FORM = {
  clientId: "",
  masterId: "",
  vehicleId: "",
  serviceId: "",
  scheduledDate: "",
  priority: "NORMAL",
  description: "",
  mileage: 1000,
};

export default function Claims() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [items, setItems] = useState([]);
  const [clients, setClients] = useState([]);
  const [masters, setMasters] = useState([]);
  const [services, setServices] = useState([]);
  const [creating, setCreating] = useState(false);
  const [updatingClaimId, setUpdatingClaimId] = useState(null);
  const [createError, setCreateError] = useState(null);
  const [actionError, setActionError] = useState(null);
  const [historyByClaim, setHistoryByClaim] = useState({});
  const [historyLoading, setHistoryLoading] = useState({});
  const [form, setForm] = useState(EMPTY_FORM);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const [claimsRes, clientsRes, servicesRes, mastersRes] = await Promise.all([
        api.get("/api/claims"),
        api.get("/api/v1/clients/"),
        api.get("/api/nsi/services"),
        api.get("/api/v1/masters"),
      ]);

      const loadedClaims = normalizeList(claimsRes.data).map((claim) => ({
        ...claim,
        claimNumber: claim.claimNumber ?? `CL-${claim.id}`,
      }));

      const loadedClients = normalizeList(clientsRes.data).map((client) => {
        if (client.firstName || client.lastName) {
          return client;
        }
        if (client.name) {
          const parts = String(client.name).trim().split(/\s+/);
          return {
            ...client,
            firstName: parts[0] ?? "",
            lastName: parts.slice(1).join(" ") || "",
          };
        }
        return client;
      });

      setItems(loadedClaims);
      setClients(loadedClients);
      setServices(normalizeList(servicesRes.data));
      setMasters(normalizeList(mastersRes.data));
    } catch (requestError) {
      setError(requestError);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const onChange = useCallback((event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }, []);

  const canCreate = useMemo(() => {
    return Boolean(form.clientId && form.masterId && form.serviceId && form.description.trim());
  }, [form.clientId, form.masterId, form.description, form.serviceId]);

  const getClientName = useCallback(
    (claim) => {
      if (claim.clientFirstName || claim.clientLastName) {
        return `${claim.clientFirstName ?? ""} ${claim.clientLastName ?? ""}`.trim();
      }

      const client = clients.find((item) => item.id === Number(claim.clientId));
      if (!client) {
        return claim.clientId ? `#${claim.clientId}` : "—";
      }

      return `${client.firstName ?? ""} ${client.lastName ?? ""}`.trim() || `#${client.id}`;
    },
    [clients]
  );

  const getMasterName = useCallback(
    (claim) => {
      if (claim.masterFirstName || claim.masterLastName) {
        return `${claim.masterFirstName ?? ""} ${claim.masterLastName ?? ""}`.trim();
      }

      if (!claim.masterId) {
        return "—";
      }

      const master = masters.find((item) => item.id === Number(claim.masterId));
      if (!master) {
        return `#${claim.masterId}`;
      }

      return `${master.firstName ?? ""} ${master.lastName ?? ""}`.trim() || `#${master.id}`;
    },
    [masters]
  );

  const getStatusLabel = useCallback((status) => {
    return STATUS_OPTIONS.find((item) => item.value === status)?.label ?? status;
  }, []);

  const getPriorityLabel = useCallback((priority) => {
    return PRIORITY_OPTIONS.find((item) => item.value === priority)?.label ?? priority ?? "—";
  }, []);

  const getStatusColor = useCallback((status) => {
    switch (status) {
      case "CREATED":
        return "bg-blue-100 text-blue-800";
      case "IN_PROGRESS":
        return "bg-yellow-100 text-yellow-800";
      case "WAITING_PARTS":
        return "bg-orange-100 text-orange-800";
      case "COMPLETED":
        return "bg-green-100 text-green-800";
      case "CANCELLED":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  }, []);

  const createClaim = useCallback(
    async (event) => {
      event.preventDefault();
      if (!canCreate) {
        return;
      }

      setCreating(true);
      setCreateError(null);

      try {
        const selectedClient = clients.find((client) => client.id === Number(form.clientId));
        const selectedClientName =
          `${selectedClient?.firstName ?? ""} ${selectedClient?.lastName ?? ""}`.trim() ||
          selectedClient?.name ||
          null;

        const createResponse = await api.post("/api/claims", {
          clientId: Number(form.clientId),
          vehicleId: Number(form.vehicleId) || 1,
          serviceId: Number(form.serviceId),
          mileageAtEntry: Number(form.mileage) || 1000,
          problemDescription: form.description.trim(),
          scheduledDate: form.scheduledDate || null,
          priority: form.priority,
          clientEmail: selectedClient?.email ?? null,
          clientName: selectedClientName,
        });
        const createdClaimId = createResponse?.data?.id;
        if (createdClaimId && form.masterId) {
          await api.put(`/api/claims/${createdClaimId}/master?masterId=${Number(form.masterId)}`);
        }

        setForm(EMPTY_FORM);
        await load();
      } catch (requestError) {
        setCreateError(requestError);
      } finally {
        setCreating(false);
      }
    },
    [canCreate, clients, form, load]
  );

  const updateStatus = useCallback(
    async (claimId, status) => {
      setUpdatingClaimId(claimId);
      setActionError(null);

      try {
        await api.put(`/api/claims/${claimId}/status`, { status });
        await load();
      } catch (requestError) {
        setActionError(requestError);
      } finally {
        setUpdatingClaimId(null);
      }
    },
    [load]
  );

  const toggleHistory = useCallback(async (claimId) => {
    if (historyByClaim[claimId]) {
      setHistoryByClaim((prev) => {
        const next = { ...prev };
        delete next[claimId];
        return next;
      });
      return;
    }

    setHistoryLoading((prev) => ({ ...prev, [claimId]: true }));
    try {
      const response = await api.get(`/api/claims/${claimId}/status-history`);
      setHistoryByClaim((prev) => ({ ...prev, [claimId]: normalizeList(response.data) }));
    } catch (requestError) {
      setActionError(requestError);
    } finally {
      setHistoryLoading((prev) => ({ ...prev, [claimId]: false }));
    }
  }, [historyByClaim]);

  return (
    <div className="space-y-6">
      <HelpPanel />

      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
          <div>
            <h2 className="text-lg font-semibold">Создание и управление заявками на обслуживание</h2>
            <p className="mt-1 text-sm text-black/60">
              Можно создавать заявки, задавать приоритет и менять статус прямо из таблицы.
            </p>
          </div>

          <button
            onClick={load}
            className="rounded-xl bg-black px-4 py-2 text-sm font-semibold text-white hover:bg-black/90"
          >
            Обновить
          </button>
        </div>
      </div>

      <div className="rounded-2xl border border-black/10 bg-white p-5">
        <h3 className="mb-4 text-base font-semibold">Создать новую заявку</h3>

        <form onSubmit={createClaim} className="grid grid-cols-1 gap-4 md:grid-cols-2">
          <div>
            <label className="mb-1 block text-sm font-medium text-black/70">Клиент *</label>
            <select
              name="clientId"
              value={form.clientId}
              onChange={onChange}
              required
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            >
              <option value="">Выберите клиента</option>
              {clients.map((client) => (
                <option key={client.id} value={client.id}>
                  {`${client.firstName ?? ""} ${client.lastName ?? ""}`.trim() || client.name || `#${client.id}`}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-black/70">Мастер *</label>
            <select
              name="masterId"
              value={form.masterId}
              onChange={onChange}
              required
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            >
              <option value="">Выберите мастера</option>
              {masters.map((master) => (
                <option key={master.id} value={master.id}>
                  {`${master.firstName ?? ""} ${master.lastName ?? ""}`.trim() || `#${master.id}`}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-black/70">Услуга *</label>
            <select
              name="serviceId"
              value={form.serviceId}
              onChange={onChange}
              required
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            >
              <option value="">Выберите услугу</option>
              {services.map((service) => (
                <option key={service.id} value={service.id}>
                  {service.name}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-black/70">Приоритет</label>
            <select
              name="priority"
              value={form.priority}
              onChange={onChange}
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            >
              {PRIORITY_OPTIONS.map((priority) => (
                <option key={priority.value} value={priority.value}>
                  {priority.label}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-black/70">Пробег</label>
            <input
              name="mileage"
              type="number"
              min="0"
              value={form.mileage}
              onChange={onChange}
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            />
          </div>

          <div className="md:col-span-2">
            <label className="mb-1 block text-sm font-medium text-black/70">Описание проблемы *</label>
            <textarea
              name="description"
              value={form.description}
              onChange={onChange}
              required
              rows={3}
              placeholder="Опишите проблему клиента"
              className="w-full rounded-xl border border-black/15 bg-white px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-orange/40"
            />
          </div>

          <div className="md:col-span-2">
            <button
              type="submit"
              disabled={!canCreate || creating}
              className="rounded-xl bg-orange px-6 py-2 text-sm font-semibold text-white disabled:opacity-50"
            >
              {creating ? "Создание..." : "Создать заявку"}
            </button>
          </div>
        </form>

        {createError ? (
          <div className="mt-3 rounded-xl bg-red-50 px-4 py-3 text-sm text-red-700">
            Ошибка создания заявки: {createError?.response?.data?.message || createError?.message}
          </div>
        ) : null}
      </div>

      {actionError ? (
        <div className="rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          Не удалось изменить статус заявки: {actionError?.response?.data?.message || actionError?.message}
        </div>
      ) : null}

      <div className="overflow-hidden rounded-2xl border border-black/10 bg-white">
        {loading ? (
          <div className="p-5 text-sm text-black/60">Загрузка...</div>
        ) : error ? (
          <div className="p-5 text-sm text-red-600">Ошибка загрузки: {error?.message}</div>
        ) : items.length === 0 ? (
          <div className="p-5 text-sm text-black/60">Заявок пока нет</div>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-black/[0.03] text-black/70">
              <tr>
                <th className="px-4 py-3 text-left font-semibold">№</th>
                <th className="px-4 py-3 text-left font-semibold">Клиент</th>
                <th className="px-4 py-3 text-left font-semibold">Мастер</th>
                <th className="px-4 py-3 text-left font-semibold">Статус</th>
                <th className="px-4 py-3 text-left font-semibold">Описание</th>
                <th className="px-4 py-3 text-left font-semibold">Приоритет</th>
                <th className="px-4 py-3 text-right font-semibold">Действия</th>
              </tr>
            </thead>
            <tbody>
              {items.map((claim) => (
                <Fragment key={claim.id}>
                  <tr className="border-t border-black/10">
                    <td className="px-4 py-3">{claim.claimNumber || claim.id}</td>
                    <td className="px-4 py-3">{getClientName(claim)}</td>
                    <td className="px-4 py-3">{getMasterName(claim)}</td>
                    <td className="px-4 py-3">
                      <span className={`inline-block rounded px-2 py-1 text-xs font-medium ${getStatusColor(claim.status)}`}>
                        {getStatusLabel(claim.status)}
                      </span>
                    </td>
                    <td className="px-4 py-3">{claim.problemDescription || "—"}</td>
                    <td className="px-4 py-3">{getPriorityLabel(claim.priority)}</td>
                    <td className="px-4 py-3 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <button
                          type="button"
                          onClick={() => toggleHistory(claim.id)}
                          className="rounded-xl border border-black/15 bg-white px-3 py-2 text-xs hover:bg-black/[0.03]"
                        >
                          {historyLoading[claim.id] ? "..." : historyByClaim[claim.id] ? "Скрыть историю" : "История"}
                        </button>
                        <select
                          value={claim.status}
                          disabled={updatingClaimId === claim.id}
                          onChange={(event) => updateStatus(claim.id, event.target.value)}
                          className="rounded-xl border border-black/15 bg-white px-3 py-2 text-xs"
                        >
                          {STATUS_OPTIONS.map((option) => (
                            <option key={option.value} value={option.value}>
                              {option.label}
                            </option>
                          ))}
                        </select>
                      </div>
                    </td>
                  </tr>
                  {historyByClaim[claim.id] ? (
                    <tr className="border-t border-black/10 bg-black/[0.02]">
                      <td className="px-4 py-2 text-xs text-black/70" colSpan={7}>
                        {historyByClaim[claim.id].length === 0 ? (
                          <span>История пустая</span>
                        ) : (
                          historyByClaim[claim.id].map((h) => (
                            <div key={h.id} className="py-1">
                              {h.createdAt || "—"}: {h.oldStatus || "—"} → {h.newStatus || "—"} {h.notes ? `(${h.notes})` : ""}
                            </div>
                          ))
                        )}
                      </td>
                    </tr>
                  ) : null}
                </Fragment>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
