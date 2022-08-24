

function previewImage(src) {

    var preview = document.querySelector('#preview');
    let imageHeight = 100;
    var image = new Image();
    image.height = imageHeight;
    image.title  = "";
    image.src    = src;
    image.className = "thumbnail-container image";

    preview.appendChild(image);
    let removeButton = document.createElement("button");
    removeButton.innerText = "X";
    removeButton.className = "thumbnail-container btn";
    removeButton.addEventListener("click", function(){
       preview.removeChild(image);
       preview.removeChild(removeButton);
       src="";
    });
    preview.appendChild(removeButton);

}
