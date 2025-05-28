# DigiLibrary GUI

DigiLibrary GUI, konsol tabanlı DigiLibrary uygulamasının Windows uyumlu grafik arayüz sürümüdür.
DigiLibrary, kullanıcıların kitaplarını dijital olarak yönetmesini, alıntılar kaydetmesini ve okuma hedefleri belirleyip ilerlemelerini takip etmesini sağlayan Java tabanlı bir masaüstü uygulamasıdır.

## Özellikler

- Kullanıcı dostu grafik arayüz
- Giriş ve kayıt ekranları
- Kitap ekleme, düzenleme, silme ve listeleme işlevleri
- Sekmeli yapı ile kolay gezinme
- Windows'ta sorunsuz çalışma
- Kullanıcı kaydı ve girişi
- Kitap ekleme, listeleme
- Alıntı oluşturma ve görüntüleme
- Okuma hedefi belirleme
- Okuma ilerlemesi takibi

## Kullanılan Nesne Yapıları

- Uygulama içerisinde nesne yönelimli programlama (OOP) prensiplerine uygun olarak aşağıdaki sınıf yapıları ve ilişkileri kullanılmıştır:

- User: Kullanıcı bilgilerini ve kimlik doğrulamasını yönetir.

- Book: Kitap adı, yazar, ISBN, yayın tarihi gibi bilgileri içerir.

- Quote: Kitaplardan alınan alıntıların metin ve bağlam bilgilerini barındırır.

- ReadingProgress: Kullanıcının kitap üzerindeki okuma ilerlemesini tutar.

- ReadingGoal: Belirlenen okuma hedeflerini temsil eder.

- Bu sınıflar, ilgili servis (UserService, BookService, vb.) ve repository (UserRepository, BookRepository, vb.) katmanlarıyla birlikte MVC mimarisine uygun şekilde yapılandırılmıştır. Arayüz işlemleri ise DigiLibraryGUI sınıfında Swing bileşenleri kullanılarak yönetilmektedir.

## Sistem Gereksinimleri

- Java 11 veya üzeri

## Kurulum ve Çalıştırma

1. Java 11 veya üzeri sürümün bilgisayarınızda kurulu olduğundan emin olun
2. JAR dosyasını indirin: `DigiLibraryGUI-1.0-SNAPSHOT-jar-with-dependencies.jar`
3. Dosyaya çift tıklayarak veya komut satırında aşağıdaki komutu çalıştırarak uygulamayı başlatın:
   ```
   java -jar DigiLibraryGUI-1.0-SNAPSHOT-jar-with-dependencies.jar
    ```
4. Veya zip dosyasını bir klasöre çıkartın
   Projeyi bir IDE (IntelliJ, VSCode, Eclipse) kullanarak açın
   ve doğrudan DigiLibraryGUI dosyasını çalıştırın
  

## Demo Kullanıcı Bilgileri

- E-posta: user@digilibrary.com
- Şifre: user123

## Kullanım

1. Giriş ekranında e-posta ve şifrenizi girin veya yeni bir hesap oluşturun
2. Ana ekranda sekmeler arasında gezinerek kitap, alıntı ve okuma ilerlemesi işlemlerini gerçekleştirin
3. Kitaplar sekmesinde kitap ekleyebilir, düzenleyebilir ve silebilirsiniz
4. Diğer işlevler (alıntılar, okuma ilerlemesi, istatistikler ve hedefler) gelecek sürümlerde tamamlanacaktır

## Veri Depolama

Uygulama, verileri `data` klasöründe JSON formatında saklar. Bu klasör, uygulamanın çalıştırıldığı dizinde otomatik olarak oluşturulur.

## Geliştirme

Bu proje Maven ile oluşturulmuştur. Kaynak kodunu derlemek için:

```
mvn clean package
```

## Lisans

Bu uygulama eğitim amaçlı olarak geliştirilmiştir.



## ENGLISH


# DigiLibrary GUI
DigiLibrary GUI is the graphical user interface version of the console-based DigiLibrary application, optimized for Windows systems.
DigiLibrary is a Java-based desktop application that enables users to manage their digital libraries, save quotes, set reading goals, and track progress.

# Features
User-friendly graphical interface

Login and registration screens

Add, edit, delete, and list books

Tab-based navigation for ease of use

Seamless performance on Windows

User registration and login

Quote creation and viewing

Set reading goals

Track reading progress

# Object-Oriented Structures
The application is built using Object-Oriented Programming (OOP) principles and includes the following class structures:

User: Manages user information and authentication.

Book: Stores data like book title, author, ISBN, and publication date.

Quote: Contains quotes from books along with their context.

ReadingProgress: Tracks users' reading progress for each book.

ReadingGoal: Represents individual reading targets.

These classes are organized using the MVC architecture, utilizing service layers (UserService, BookService, etc.) and repository layers (UserRepository, BookRepository, etc.). The graphical interface is built using Swing components in the DigiLibraryGUI class.

# System Requirements
Java 11 or higher

# Installation and Execution
Ensure Java 11 or later is installed on your machine.

Download the JAR file:
DigiLibraryGUI-1.0-SNAPSHOT-jar-with-dependencies.jar

Launch the application by double-clicking the file or running the following command in a terminal:

bash
Copy
Edit
java -jar DigiLibraryGUI-1.0-SNAPSHOT-jar-with-dependencies.jar

Alternatively, extract the ZIP file into a folder, open the project using an IDE (IntelliJ, VSCode, Eclipse), and run the DigiLibraryGUI class directly.

# Demo User Credentials
Email: user@digilibrary.com

Password: user123

# Usage
Enter your email and password on the login screen, or create a new account.

Navigate through tabs to manage books, quotes, and reading progress.

On the “Books” tab, you can add, edit, or delete books.

Other features (quotes, reading progress, statistics, goals) will be fully developed in future versions.

# Data Storage
The application stores data in JSON format under the data folder, which is automatically created in the running directory.

# Development
This project is built using Maven. To compile the source code:

bash
Copy
Edit
mvn clean package

# License
This application is developed for educational purposes.