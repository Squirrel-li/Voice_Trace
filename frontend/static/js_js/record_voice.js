document.addEventListener('DOMContentLoaded', function () {
  const recordBtn = document.getElementById('recordBtn');
  const recordModal = document.getElementById('recordModal');
  const closeRecordModal = document.getElementById('closeRecordModal');

  if (recordBtn && recordModal && closeRecordModal) {
    // 開啟錄音視窗
    recordBtn.addEventListener('click', () => {
      recordModal.classList.add('open');
    });

    // 關閉（右上角 X）
    closeRecordModal.addEventListener('click', () => {
      recordModal.classList.remove('open');
    });

    // 點背景關閉
    recordModal.addEventListener('click', (e) => {
      if (e.target === recordModal) {
        recordModal.classList.remove('open');
      }
    });
  }
});
