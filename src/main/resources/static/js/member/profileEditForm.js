'use strict';

function showImage(input) {
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    reader.onload = function (e) {
      document.getElementById('profileImgUrl').src = e.target.result;
      document.getElementById('profileImgUrl').style.display = 'block';
    };
    reader.readAsDataURL(input.files[0]);
  }
}

function deleteImage() {
  document.getElementById('profileImgUrl').src = '/img/default.png'; // 기본 이미지로 변경
}

document.getElementById('modifyBtn').addEventListener('click', function () {
  document.getElementById('modifyForm').submit();
});

function verifyEmail() {
  const emailInput = document.getElementById('companyEmail').value;

  // 이메일 입력 확인
  if (!emailInput) {
    document.getElementById('emailVerificationStatus').textContent = '이메일을 입력해주세요.';
    return;
  }

  // 이메일 인증 요청 전송
  fetch('/api/v1/user/sendVerificationEmail', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email: emailInput }),  // 이메일을 JSON으로 서버에 전송
  })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          document.getElementById('emailVerificationStatus').textContent = '인증 이메일이 발송되었습니다.';
        } else {
          document.getElementById('emailVerificationStatus').textContent = '이메일 인증에 실패했습니다.';
        }
      })
      .catch(error => {
        document.getElementById('emailVerificationStatus').textContent = '오류가 발생했습니다.';
        console.error('Error:', error);
      });
}