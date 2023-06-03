const getCookie = (cname) => {
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
const authCall = async (token) => {

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
                    break;
                default:
                    location.href = baseUrl + "login";
                    break;
            }
        })
}

const logOut = () => {
    const token = getCookie("token")

    $.ajax({
        method: "POST",
        url: baseUrl + "api/logout",
        data: {
            "token" : token
        }
    })
        .done(function(result){
            switch(result.apiStatusCode){
                case 200:
                    location.href = baseUrl + "login";
                    break;
                default:
                    console.log(result)
                    break;
            }
        })
}

const token = getCookie("token")
authCall(token)


$(window).on("load", function(){
    console.log($("#logout-button"))

    $("#logout-button").on("click",function(){
        console.log("logout")
        logOut()
    })
    $(`#${page}-li`).addClass("active")
})
