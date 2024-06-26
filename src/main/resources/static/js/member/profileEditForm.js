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