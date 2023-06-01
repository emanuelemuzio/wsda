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
                    break;
                default:
                    location.href = baseUrl + "login";
                    break;
            }
        })
}

$(`#${page}-li`).addClass("active")
