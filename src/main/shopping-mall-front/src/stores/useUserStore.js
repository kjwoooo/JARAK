import create from 'zustand';

const useUserStore = create(set => ({
  user: null,
  login: (userData) => set({ user: userData }),
  logout: () => set({ user: null }), // 로그아웃 함수 추가
}));

export default useUserStore;
