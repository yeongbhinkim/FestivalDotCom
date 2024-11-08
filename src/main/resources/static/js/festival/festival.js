'use strict';

let page = 1; // 페이지 번호 초기화
const size = 10; // 페이지 크기 설정
const scrollContainer = document.querySelector('.main'); // 스크롤이 있는 요소 선택

// 스크롤 이벤트 리스너 추가
scrollContainer.addEventListener('scroll', function () {
    const scrollPosition = scrollContainer.scrollTop + scrollContainer.clientHeight;
    const threshold = scrollContainer.scrollHeight - 50;

    // console.log(`Scroll Position: ${scrollPosition}`);
    // console.log(`Threshold: ${threshold}`);

    // 스크롤 위치가 페이지 하단에 도달할 때마다 추가 데이터 로드
    if (scrollPosition >= threshold) {
        // console.log('Scroll reached bottom - Loading more festivals');
        loadMoreFestivals();
    }
});

function loadMoreFestivals() {
    const festivalName = document.getElementById('festivalName').value; // 검색어 값 가져오기
    const url = `/api/v1/festival/searchScroll?festivalName=${festivalName}&page=${page}&size=${size}`;

    // console.log(`Fetching data from: ${url}`);
    // console.log(`Current page: ${page}, Size: ${size}, Festival Name: ${festivalName}`);

    // AJAX 요청으로 추가 데이터를 로드 (page와 size 값을 URL에 포함)
    fetch(url)
        .then(response => {
            // console.log(`Response status: ${response.status}`);
            return response.json();
        })
        .then(festivals => {
            // console.log(`Data received for page ${page}:`, festivals);

            if (festivals.length > 0) {
                const festivalContainer = document.getElementById('festivalContainer').querySelector('ul');
                festivals.forEach(festival => {
                    const festivalItem = document.createElement('li');
                    festivalItem.innerHTML = `
                            <div class="image-container">
                                <a href="/api/v1/festival/detail/${festival.festivalId}">
                                    <img src="${festival.festivalImgUrl}" alt="Festival Image">
                                </a>
                                <div class="image-content">
                                    <a href="/api/v1/festival/detail/${festival.festivalId}">
                                        <p>${festival.festivalName}</p>
                                    </a>
                                </div>
                            </div>
                        `;
                    festivalContainer.appendChild(festivalItem);
                });
                // console.log(`Page ${page} appended to the list.`);
                page++; // 페이지 번호 증가
            } else {
                // 데이터가 더 이상 없으면 스크롤 이벤트 제거
                // console.log('No more data to load. Removing scroll event listener.');
                scrollContainer.removeEventListener('scroll', loadMoreFestivals);
            }
        })
        .catch(error => {
            console.error('Error fetching festivals:', error);
        });
}
