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

const createCreditCardList = async (cardContainer) => {
    const token = getCookie("token")

    $.ajax({
        method: "POST",
        url: baseUrl + "api/get_credit_card_list",
        data: {
            "token" : token
        }
    })
        .done(function(result){
            switch(result.apiStatusCode){
                case 200:
                    console.log(result)
                    result.apiMessage.forEach(c => cardContainer.append(createCard(c)))
                    break;
                default:
                    break;
            }
        })
}

authCall();

$(document).ready(async function() {
    const cardContainer = $("#card-container")
    await createCreditCardList(cardContainer)
})

const createCard = (cardData) => {
    const cardDiv = $("<div></div>")
    cardDiv.addClass("card")
    cardDiv.addClass("mx-2")
    cardDiv.addClass("my-1")

    const cardHeader = $("<div></div>").text(cardData.cardNumber)
    cardHeader.addClass("card-header")

    const cardBody = $("<div></div>")
    cardBody.addClass("card-body")

    const cardTitle = $("<h5></h5>").text(cardData.ownerName)
    cardTitle.addClass("card-title")

    const cardText = $("<p></p>").text("" + cardData.cardBalance + "€")
    cardTitle.addClass("card-text")

    cardDiv.append(cardHeader)
    cardDiv.append(cardBody)
    cardBody.append(cardTitle)
    cardBody.append(cardText)

    return cardDiv
}