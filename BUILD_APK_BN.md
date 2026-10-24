# আমাদের ব্লক — APK তৈরির নির্দেশিকা

এই ZIP-এ Android প্রজেক্টের সোর্স কোড আছে। এটি নিজে থেকে ইনস্টলযোগ্য APK নয়।

GitHub Actions দিয়ে বিল্ড করতে Repository-তে সোর্স ফাইলগুলি (বিশেষ করে `app/`, `settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`) থাকতে হবে। Workflow ফাইলটি `.github/workflows/android.yml`-এ রাখুন।

যদি Repository-তে `AmaderBlock_Android_Starter.zip` নামে এই ZIP আপলোড করা থাকে, workflow সেটি extract করে `gradle assembleDebug` চালাবে। GitHub Actions-এর `Actions` ট্যাব থেকে `Build AmaderBlock APK` চালু করুন। সফল হলে run-এর `Artifacts` অংশ থেকে `AmaderBlock-APK` ডাউনলোড করুন।

এটি প্রাথমিক প্রোটোটাইপ; সরকারি পরিষেবার সঙ্গে বাস্তব সংযোগ, নিরাপদ অ্যাডমিন লগইন, অভিযোগ জমা/ট্র্যাকিং এবং গোপনীয়তা নীতি যোগ করা বাকি।
