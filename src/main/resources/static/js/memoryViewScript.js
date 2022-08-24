function change_memory_view(memory){

    var id = memory.id;

    var location = memory.location;
    if (!location || location == 'null'){
        var locationId = 'name-location-' + id;
        var locationElement = document.getElementById(locationId);
        locationElement.textContent = memory.name;
    }

}