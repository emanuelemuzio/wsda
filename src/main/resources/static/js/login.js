function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

const loginFunction = async () => {

    const email = $("input[type='email']").val()
    const password = $("input[type='password']").val()

    const credentials = btoa(email + ":" + password)

    $.ajax({
        method: "POST",
        url: baseUrl + "api/login",
        data: {
            "credentials" : credentials
        }
    })
        .done(function(result){
            switch(result.apiStatusCode){
                case 200:
                    document.cookie = "token=" + result.apiMessage
                    location.href = baseUrl + "dashboard"
                    break;
                default:
                    $("#login-response-err").css("display","block")
                    $("#login-response-err").text(result.apiMessage)
                    break;
            }
        })
}

const authCall = async () => {
    const token = getCookie("token")

    $.ajax({
        method: "POST",
        url: baseUrl + "api/auth",
        data: {
            "token" : token
        }
    })
        .done(function(result){
            switch(result.apiStatusCode){
                case 200:
                    location.href = baseUrl + "dashboard";
                    break;
                default:
                    break;
            }
        })
}

authCall();

$(document).ready(function(){
    $("#login-form").on("submit", function(f){
        f.preventDefault();
        loginFunction()
    })
});

$(window).on('load',function(){
    $(document).on('keypress', function(e){
        if(e.which == 13){
            loginFunction()
        }
    })
});


