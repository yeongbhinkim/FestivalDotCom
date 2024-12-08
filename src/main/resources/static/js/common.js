document.addEventListener('DOMContentLoaded', function () {
    const listItems = document.querySelectorAll('li');
    listItems.forEach(item => {
        item.addEventListener('click', function () {
            const link = item.querySelector('a');
            if (link) {
                link.click(); // `a` 태그의 클릭 이벤트 트리거
            }
        });
    });
});
