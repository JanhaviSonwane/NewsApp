# NewsApp
🚀 Features

📰 News Feed
	•	Displays top headlines from a public News API
	•	Each article shows:
	•	Title
	•	Featured Image
	•	Source
	•	Published Date
	•	Short Description
	•	Infinite scrolling using Paging 3
	•	Loading, Error, and Empty UI states

🔍 Search
	•	Search news by keyword
	•	Debounced search using Kotlin Flow
	•	Dedicated loading/error/empty states

📄 Article Details
	•	View article in a WebView
	•	Built-in actions:
	•	Share article
	•	Open in browser
	•	Bookmark / Unbookmark

⭐ Bookmarks
	•	Save articles locally
	•	View in dedicated Bookmarks screen
	•	Works offline
	•	Bookmark icon toggles instantly
	•	Delete bookmarks

💾 Offline Persistence
	•	Bookmarks stored in Room database
	•	Clean migration-ready DB structure

⚙️ Architecture
	•	MVVM + Clean Architecture
	•	Repository pattern
	•	UseCases (optional)
	•	UI layer built with Jetpack Compose
	•	State management using StateFlow

🧪 Testing (Optional/Extendable)
	•	Unit Tests for ViewModel & Repository
	•	UI Tests using Compose Test
