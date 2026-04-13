export function normalizeList(data) {
  if (!data) return [];
  if (Array.isArray(data)) return data;
  if (Array.isArray(data.items)) return data.items;
  if (Array.isArray(data.content)) return data.content;
  if (Array.isArray(data.data)) return data.data;
  return [];
}

