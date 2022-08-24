function change_wine_review_view(wineReview){

    var id = wineReview.id;

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