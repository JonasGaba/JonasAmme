/*<![CDATA[*/
function change_wine_review_view(wineReview){


    var id = wineReview.id;

    var rating = wineReview.rating;
    if(rating && rating != 'null'){
        var starPercentage = (Number(rating) / 5) * 100;
        var starPercentageRounded = Math.round(starPercentage / 10) * 10;
        var starPercentageString = '' + starPercentageRounded + '%';
        var starId = 'inner-star-' + id;
        var innerStarElement = document.getElementById(starId);
        innerStarElement.style.width = starPercentageString;
    }


    var year = wineReview.year;
    if (!year || year == 'null'){
        var yearId = 'name-year-' + id;
        var yearElement = document.getElementById(yearId);
        yearElement.textContent = wineReview.name;
    }
    var price = wineReview.price;
    if (!price || price == 'null'){
        var priceId = 'type-price-' + id;
        var priceElement = document.getElementById(priceId);
        priceElement.textContent = wineReview.type;
    }

}
/*]]>*/