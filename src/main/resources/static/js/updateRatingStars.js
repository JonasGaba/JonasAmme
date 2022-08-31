function updateRatingStars(reviewObject){


    var id = reviewObject.id;

    var rating = reviewObject.rating;
    if(rating && rating != 'null'){
        var starPercentage = (Number(rating) / 5) * 100;
        var starPercentageRounded = Math.round(starPercentage / 10) * 10;
        var starPercentageString = '' + starPercentageRounded + '%';
        var starId = 'inner-star-' + id;
        var innerStarElement = document.getElementById(starId);
        innerStarElement.style.width = starPercentageString;
    }

}