import create from 'zustand';

//const useUserStore = create(set => ({
//  user: null,
//  login: (userData) => set({ user: userData }),
//  logout: () => set({ user: null }), // 로그아웃 함수 추가
//}));

const useUserStore = create(set => ({
  user: JSON.parse(localStorage.getItem('user')), // 초기 상태를 로컬 스토리지에서 가져옴
  login: (userData) => {
    localStorage.setItem('user', JSON.stringify(userData)); // 로컬 스토리지에 저장
    set({ user: userData });
  },
  logout: () => {
    localStorage.removeItem('user'); // 로컬 스토리지에서 제거
    set({ user: null });
  },
}));

export default useUserStore;
