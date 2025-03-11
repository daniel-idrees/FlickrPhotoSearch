An image search Android App that enables users to search images and keep a track of runtime searches.

#### Features

Search Functionality – Users enter a query, and the app fetches related images.
Search History – A list of previous searches is maintained during the app's runtime. User can also delete the search history.
Image Display – The app shows search results in a scrollable list.
State Management – Search history may reset when the app is restarted

#### Data Source

The App uses [Flickr API](https://www.flickr.com/services/api/flickr.photos.search.html) to retrieve photos and relatable information

#### API key

Ensure that your project's ```local.properties``` file contains the Flickr API key, like this:

```API_KEY="your_api_key_here"```

#### Technologies and Tools 

- Kotlin 
- Model-View-ViewModel (MVI) architectural pattern
- App Architecture as per [Google's Recommendation](https://developer.android.com/topic/architecture)
- Multi Module
- Hilt for dependency injection
- Coroutines for asynchronous operations
- Flows for handling data asynchronously
- Jetpack Compose for building UI
- Retrofit for network calls
- Junit/Mockito/Kotest for unit testing 
- Coil for Image loading.

#### Demo

TO-DO

#### Possible Enhancements
- Filtering search results
- Local storage of search history 
- Caching results to provide offline access
- Bookmarking images 
- Scrollable Grid layout for landscape orientation to utilize horizontal space
