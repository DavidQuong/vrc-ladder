export const go = {
  pattern: 'go to :url',
  action: ({session}, url) => session.get(url),
};
