

function previewImage(src, id) {

    var preview = document.querySelector('#preview');
    const imgDiv = document.createElement("div");
    imgDiv.className = "thumbnail-container-img";
    let imageHeight = 100;
    var image = new Image();
    image.height = imageHeight;
    image.title  = "";
    image.src    = src;

    imgDiv.appendChild(image);
    let removeButton = document.createElement("button");
    removeButton.innerText = "X";
    removeButton.className = "thumbnail-container btn";
    var photos = document.getElementById('photos');
    var photosToDelete = document.getElementById('photosToDelete');
    removeButton.addEventListener("click", function(){
       preview.removeChild(imgDiv);
        photosToDelete.value += id + ",";
    });
    imgDiv.appendChild(removeButton);
    preview.appendChild(imgDiv);

}
