# project-patricia-ceausene

# EcoTracker App

Aplicatia ofera utilizatorului un calculator de carbon care foloseste date din 3 categorii precum casa, transport si calatorii. In urma calculului, aplicatia ofera un raport personalizat si diverse metode prin care utilizatorul isi poate diminua amprenta de carbon. In plus, pentru a motiva utilizatorii, aplicatia include mai multe elemente de gamificare precum un sistem de punctare, clasamente nationale, optiunea de a automatiza un obicei sanatos si trimitirea de notificari.

Autentificarea utilizatorilor se realizeaza prin e-mail si parola, cu ajutorul **Firebase Authentification**. De asemenea, datele utilzatorilor sunt salvate in **Cloud Firestore**, o baza de date NoSQL sigura, si **Firebase Storage** pentru stocarea imaginilor de profil.

Aplicatia urmareste o arhitectira de tip **MVVM (Model-View-ViewModel)**. Navigarea intre ecrane este realizata cu ajutorul bibliotecii **Jetpack Navigation**.


Pentru calcului amprentei de carbon, este folosit un **API online** oferit de [Engie](https://www.engie.ro/amprenta-de-carbon/ "Calculator de carbon")

Pentru declansarea de notificari, este folosit un manager de alarme (_AlarmManager_) si mesajele de difuzare (_BroadcastReceiver_).

Pentru buna functionare, sunt implementate **teste unitare** cu ajutorul bibliotecii _JUnit_.