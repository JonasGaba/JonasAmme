let fileSet = new Set();
let imageHeight = 100;
function previewImages() {

  var preview = document.querySelector('#preview');

  for (var i = 0; i < this.files.length; i++){
       fileSet.add(this.files[i]);
  }
  
  if (this.files) {
    [].forEach.call(this.files, readAndPreview);
  }

  setFiles(this, fileSet);

  function readAndPreview(file) {

    // Make sure `file.name` matches our extensions criteria
    if (!/\.(jpe?g|png|gif)$/i.test(file.name)) {
      return alert(file.name + " is not an image");
    } // else...

    var reader = new FileReader();
    reader.addEventListener("load", function() {

      var image = new Image();
      image.height = imageHeight;
      image.title  = file.name;
      image.src    = this.result;
      image.className = "thumbnail-container image";

      preview.appendChild(image);
      let removeButton = document.createElement("button");
      removeButton.innerText = "X";
      removeButton.className = "thumbnail-container btn";
      removeButton.addEventListener("click", function(){
         preview.removeChild(image);
         preview.removeChild(removeButton);
         fileSet.delete(file);
         setFiles(document.querySelector('#file-input'), fileSet);
      });
      preview.appendChild(removeButton);

    });
    
    reader.readAsDataURL(file);
  }


}

function setFiles(input, files){
  const dataTransfer = new DataTransfer();
  for(const file of files)
    dataTransfer.items.add(file);
  input.files = dataTransfer.files;
}

document.querySelector('#file-input').addEventListener("change", previewImages);
