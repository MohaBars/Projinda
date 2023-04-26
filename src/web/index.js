//Declare varables
const container = document.querySelector('.container');
const searchBox = document.querySelector('.search-box button');
const notFound = document.querySelector('.not-found');
const weatherBox = document.querySelector('.weather-box');
const inputField = document.querySelector('.search-box input');

//If search button is clicked, call the function searchWethaer
searchBox.addEventListener('click', searchWeather);
//If Enter key is pressed, call the function searchWethaer
inputField.addEventListener('keypress', function(event) {
    // Check if the pressed key is Enter
    if (event.key === 'Enter' || event.keyCode === 13) {
        searchWeather();
    }
});

function searchWeather() {
    const city = inputField.value.toLowerCase();
    var results;

    if(city === ''){
        return;
    }

    //use fetch() method to send and recieve strings
    fetch ('http://localhost:8080/weather?query=' + city)
        .then(response => response.text())
        .then(data => {

            //Handles the case where the city is not found
            if(data.includes('Error')){
                container.style.height = '700px';
                weatherBox.style.display = 'none';
                notFound.style.display = 'block';
                notFound.classList.add('fadeIn');
                return;
            }

            //Otherwise the error image will not be displayed
            notFound.style.display = 'none';
            notFound.classList.remove('fadeIn');

            const image = document.querySelector('.weather-box img');
            const description = document.querySelector('.weather-box .description');

            //switch case to check the response
            switch (true){
                case data.includes('rain'):
                    image.src = 'images/rain.jpeg';
                    break;

                case data.includes('clouds'):
                    image.src = 'images/rain.jpeg';
                    break;

                case data.includes('snow'):
                    image.src = 'images/rain.jpeg';
                    break;

                case data.includes('clear'):
                    image.src = 'images/rain.jpeg';
                    break;

                default:
                    image.src = '';
            }

            description.innerHTML = data;

            weatherBox.style.display = '';
            weatherBox.classList.add('fadeIn');
            container.style.height = '700px';
        });
}