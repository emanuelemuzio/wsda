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


const authCall = async () => {
    const token = getCookie("token")
    $.ajax({
        method: "POST",
        url: "auth",
        data: {
            "token" : token
        }
    })
        .done(function(result){
            if(result.login === true){
                location.href = "http://localhost:9000/backend/home"
            }
        })
}

authCall();

$(document).ready(function(){

    $("#login-btn").on("click", function(){
        const email = $("input[type='email']").val()
        const password = $("input[type='password']").val()

        const credentials = btoa(email + ":" + password)

        $.ajax({
            method: "POST",
            url: "login",
            data: {
                "credentials" : credentials
            }
        })
            .done(function(result){
                if(result.token != null && result.login === true){
                    document.cookie = "token=" + result.token
                    location.href = "http://localhost:9000/backend/home"
                }
                else{
                    $("#login-response-err").css("display","block")
                }
            })
    })
});
