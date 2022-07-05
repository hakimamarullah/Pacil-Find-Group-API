## Pacil Find Group

[![Quality Gate Status](https://sonarqube.cs.ui.ac.id/api/project_badges/measure?project=AdvProg_reguler-2022_student_kelas-c-blended_1906293184-Muhammad-Faisal-Adi-Soesatyo_pfg-be_AX_km7R_mTzPxwcesCtp&metric=alert_status)](https://sonarqube.cs.ui.ac.id/dashboard?id=AdvProg_reguler-2022_student_kelas-c-blended_1906293184-Muhammad-Faisal-Adi-Soesatyo_pfg-be_AX_km7R_mTzPxwcesCtp)
[![Code Smells](https://sonarqube.cs.ui.ac.id/api/project_badges/measure?project=AdvProg_reguler-2022_student_kelas-c-blended_1906293184-Muhammad-Faisal-Adi-Soesatyo_pfg-be_AX_km7R_mTzPxwcesCtp&metric=code_smells)](https://sonarqube.cs.ui.ac.id/dashboard?id=AdvProg_reguler-2022_student_kelas-c-blended_1906293184-Muhammad-Faisal-Adi-Soesatyo_pfg-be_AX_km7R_mTzPxwcesCtp)
[![coverage report](https://gitlab.cs.ui.ac.id/AdvProg/reguler-2022/student/kelas-c-blended/1906293184-Muhammad-Faisal-Adi-Soesatyo/pfg-be/badges/dev/coverage.svg)](https://gitlab.cs.ui.ac.id/AdvProg/reguler-2022/student/kelas-c-blended/1906293184-Muhammad-Faisal-Adi-Soesatyo/pfg-be/-/commits/dev)
[![pipeline status](https://gitlab.cs.ui.ac.id/AdvProg/reguler-2022/student/kelas-c-blended/1906293184-Muhammad-Faisal-Adi-Soesatyo/pfg-be/badges/dev/pipeline.svg)](https://gitlab.cs.ui.ac.id/AdvProg/reguler-2022/student/kelas-c-blended/1906293184-Muhammad-Faisal-Adi-Soesatyo/pfg-be/-/commits/dev)
___
Pacil Find Group API merupakan API yang digunakan untuk membuat kelompok dan mencari anggota kelompok pada suatu mata
kuliah. API ini melayani pembuatan grup baru untuk merekrut anggota, proses login dan register, pengajuan lamaran untuk
menjadi anggota kelompok, dan menambahkan course/mata kuliah baru (hanya dapat dilakukan oleh pengguna dengan level
superuser). Saat ini, API ini hanya dapat digunakan oleh mahasiswa Universitas Indonesia khususnya mahasiswa Fasilkom UI
yang memiliki akun email UI.

### Tech Stack

* SpringBoot `v2.6.4`
* Gradle `v7.4`
* PostgreSQL (heroku)
* Heroku (Deployment)
* H2-Database `v2.1.210` (Only for testing purposes)
* Authentication: Spring security and JWT Token (Stateless policy)
* SonarQube `v3.0`

### How to run locally

* Install Gradle `v7.4`. [Download Gradle 7.4.1](https://downloads.gradle-dn.com/distributions/gradle-7.4.1)
* See Gradle installation manual page [here](https://gradle.org/install/#manually)
* Go to root project folder then run SpringBoot using command `gradle bootRun`

# Pacil Find Group 1.0 API Release Notes

## Sprint 1.0

## Fitur

* **`/api/v1/auth/authenticate`** - Endpoint untuk request JWT token bagi pengguna yang telah terdaftar.
* **`/api/v1/auth/register`** - Endpoint untuk mendaftarkan pengguna.
* **`/api/v1/group/all`** - Mengembalikan semua grup yang sedang aktif dan masih membuka lowongan (completed=false).
* **`/api/v1/group/create-group`** - Endpoint untuk membuat grup baru.
* **`/api/v1/group/delete-group/{id}`** - Menghapus grup.
* **`/api/v1/group/close/{id}`** - Mengakhiri proses recruitment anggota dengan mengubah atribut `completed`
  menjadi `true`.
* **`/api/v1/group/search`** - Menggunakan parameter courseName untuk mencari group berdasarkan nama course.

## Bug fixes

* Mengizinkan CORS request.

# Pacil Find Group 2.0 API Release Notes

## Sprint 2.0

## New Features

* **/api/v1** - for Large Record Contract Creation Constant memory usage for file creation and storage initalization of
  ingestion. Added support for hash maps.
* **/api/v1** - supports constant keyword and variable initalization.
* **Query Pagination** - support for offset and limit when querying Cirrus in order to control response size over large
  datasets.
* **/api/v1** - increased contract creation transactions per second.
* **/api/v1** - Kafka has been upgraded to 1.1.0.

## Hotfixes

* **N/A**

# Pacil Find Group 3.0 API Release Notes

## Sprint 3.0

## New Features

* **N/A**

## Bug Fixes

* **N/A**
