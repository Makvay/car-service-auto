import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

const resources = {
  ru: {
    translation: {
      dashboard: 'Дашборд',
      claims: 'Заявки',
      clients: 'Клиенты',
      masters: 'Мастера',
      warehouse: 'Склад',
      notifications: 'Уведомления',
      nsi: 'НСИ',
      logout: 'Выйти',
      save: 'Сохранить',
      cancel: 'Отмена',
      reset: 'Сброс',
      create: 'Создать',
      edit: 'Редактировать',
      delete: 'Удалить',
      update: 'Обновить',
      search: 'Поиск...',
      noData: 'Нет данных',
      loading: 'Загрузка...',
      error: 'Ошибка',
      success: 'Успешно',
      session: 'Сессия истекает через:',
      sessionExpired: 'Сессия истекла',
      welcome: 'Добро пожаловать',
      yourClaimCreated: 'Ваша заявка создана',
      claimStatusChanged: 'Статус заявки изменён',
      workCompleted: 'Ваш автомобиль готов',
      login: 'Войти',
      loginRequired: 'Требуется авторизация',
      checkKeycloak: 'Проверьте доступность Keycloak и настройки клиента',
      help: 'Справка',
      hideHelp: 'Скрыть справку',
      showHelp: 'Показать справку',
      userManual: 'Руководство пользователя',
      orderOfWork: 'Порядок работы',
      sectionDescription: 'Описание разделов',
      howToCreateClaim: 'Как создать заявку',
      howToReserveParts: 'Как зарезервировать запчасти',
      claimStatuses: 'Статусы заявок',
      documentation: 'Документация'
    }
  },
  en: {
    translation: {
      dashboard: 'Dashboard',
      claims: 'Claims',
      clients: 'Clients',
      masters: 'Masters',
      warehouse: 'Warehouse',
      notifications: 'Notifications',
      nsi: 'NSI',
      logout: 'Logout',
      save: 'Save',
      cancel: 'Cancel',
      reset: 'Reset',
      create: 'Create',
      edit: 'Edit',
      delete: 'Delete',
      update: 'Update',
      search: 'Search...',
      noData: 'No data',
      loading: 'Loading...',
      error: 'Error',
      success: 'Success',
      session: 'Session expires in:',
      sessionExpired: 'Session expired',
      welcome: 'Welcome',
      yourClaimCreated: 'Your claim has been created',
      claimStatusChanged: 'Claim status changed',
      workCompleted: 'Your car is ready',
      login: 'Login',
      loginRequired: 'Authorization required',
      checkKeycloak: 'Check Keycloak availability and client settings',
      help: 'Help',
      hideHelp: 'Hide help',
      showHelp: 'Show help',
      userManual: 'User Manual',
      orderOfWork: 'Order of Work',
      sectionDescription: 'Section Description',
      howToCreateClaim: 'How to create a claim',
      howToReserveParts: 'How to reserve parts',
      claimStatuses: 'Claim statuses',
      documentation: 'Documentation'
    }
  }
};

i18n
  .use(initReactI18next)
  .init({
    resources,
    lng: 'ru',
    fallbackLng: 'ru',
    interpolation: {
      escapeValue: false
    }
  });

export default i18n;
