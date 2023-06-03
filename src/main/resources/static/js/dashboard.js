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
                    result.apiMessage.forEach(c => cardContainer.append(createCard(c)))
                    break;
                case 401:
                    location.href = baseUrl + "login";
                    break;
                default:
                    console.log(result)
                    break;
            }
        })
}

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

$(document).ready(async function() {
    const cardContainer = $("#card-container")
    await createCreditCardList(cardContainer)
})