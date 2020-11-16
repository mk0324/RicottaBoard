<html>
<head>
    <#include "css/style.css">
</head>
<div class="temp_body">


    <div class="bodyContent">

        <div class="headlineContent">
            <h1>Ricotta board</h1>
        </div>

        <div class="innerContent">


            <h2>${userName}님 안녕하세요! </h2>

            <div class="mainText">
                <p>안녕하세요. 리코타 보드입니다.</p>
                <p>가입해주셔서 감사합니다! :)</p>
                <p>무분별한 가입을 막기위해 최초 한번의 이메일 인증 과정이 필요합니다.</p>
                <p>회원가입을 완료하기 위해 아래의 Confirm 버튼을 눌러 계정을 활성화 시켜주세요.</p>
                <p>회원님이 가입한 것이 아니라면 해당 이메일은 무시하셔도 좋습니다.</p>
            </div>

            <div class="buttonHere">
                <a class="button__a" href="${userEmailTokenVerificationLink}" target="_blank" rel="noopener">
                    Confirm email</a>
            </div>

            <div class="andLast">
                <p style="font-size:18px">자세한 내용은 리코타 보드 홈페이지를 참고하세요.</p>
                <p style="text-align: right; font-size:18px;">- Hungry Bird -</p>
            </div>
        </div>

    </div>


    <div class="footer">
        <p>© 2020 Ricotta Board</p>
        <p>508, Eonju-ro, Gangnam-gu, Seoul, 06152 - Republic of Korea</p>
    </div>
</div>

</html>