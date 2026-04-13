import { useState } from "react";
import { useTranslation } from "react-i18next";

export default function HelpPanel() {
  const { t } = useTranslation();
  const [showHelp, setShowHelp] = useState(false);

  return (
    <div className="mb-6">
      <button
        onClick={() => setShowHelp(!showHelp)}
        className="text-sm text-orange font-medium hover:underline"
      >
        {showHelp ? t("hideHelp") : t("showHelp")}
      </button>

      {showHelp && (
        <div className="mt-4 p-5 bg-gradient-to-br from-blue-50 to-indigo-50 rounded-2xl border border-blue-200 text-sm">
          <h3 className="font-bold text-lg mb-4 text-blue-900">📋 {t("userManual")}</h3>
          
          <div className="space-y-5">
            <div>
              <h4 className="font-semibold text-blue-800 mb-2">🔄 {t("orderOfWork")}</h4>
              <ol className="list-decimal list-inside space-y-1 text-blue-700 ml-2">
                <li>Создайте <strong>Клиентов</strong> (Clients)</li>
                <li>Добавьте <strong>Мастеров</strong> (Masters)</li>
                <li>Создайте <strong>Заявку</strong> (Claims)</li>
                <li>Зарезервируйте <strong>Запчасти</strong> (Warehouse)</li>
                <li>Следите за <strong>Уведомлениями</strong></li>
              </ol>
            </div>

            <div>
              <h4 className="font-semibold text-blue-800 mb-2">📁 {t("sectionDescription")}</h4>
              <ul className="space-y-1 text-blue-700 ml-2">
                <li><strong>Dashboard</strong> — обзор и статистика</li>
                <li><strong>Claims (Заявки)</strong> — заявки на обслуживание</li>
                <li><strong>Clients (Клиенты)</strong> — клиенты и автомобили</li>
                <li><strong>Masters (Мастера)</strong> — мастера и специализации</li>
                <li><strong>Warehouse (Склад)</strong> — запчасти и остатки</li>
                <li><strong>Notifications</strong> — уведомления</li>
                <li><strong>NSI</strong> — справочники</li>
              </ul>
            </div>

            <div>
              <h4 className="font-semibold text-blue-800 mb-2">📝 {t("howToCreateClaim")}</h4>
              <ol className="list-decimal list-inside space-y-1 text-blue-700 ml-2">
                <li>Перейдите в <strong>Claims</strong></li>
                <li>Укажите ID клиента и ID услуги</li>
                <li>Выберите дату</li>
                <li>Нажмите "Создать"</li>
              </ol>
            </div>

            <div>
              <h4 className="font-semibold text-blue-800 mb-2">🔧 {t("howToReserveParts")}</h4>
              <ol className="list-decimal list-inside space-y-1 text-blue-700 ml-2">
                <li>Перейдите в <strong>Warehouse</strong></li>
                <li>Укажите ID заявки</li>
                <li>Укажите ID запчасти</li>
                <li>Укажите количество</li>
                <li>Нажмите "Зарезервировать"</li>
              </ol>
            </div>

            <div>
              <h4 className="font-semibold text-blue-800 mb-2">🏷️ {t("claimStatuses")}</h4>
              <div className="flex flex-wrap gap-2 mt-2">
                <span className="px-2 py-1 bg-blue-100 text-blue-800 rounded text-xs font-medium">NEW — Новая</span>
                <span className="px-2 py-1 bg-yellow-100 text-yellow-800 rounded text-xs font-medium">IN_PROGRESS — В работе</span>
                <span className="px-2 py-1 bg-orange-100 text-orange-800 rounded text-xs font-medium">WAITING_PARTS — Ожидает запчасти</span>
                <span className="px-2 py-1 bg-green-100 text-green-800 rounded text-xs font-medium">DONE — Готова</span>
                <span className="px-2 py-1 bg-red-100 text-red-800 rounded text-xs font-medium">CANCELLED — Отменена</span>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
