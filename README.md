# CVScanner
 
Spring Batch əsaslı, CV-ləri (PDF/DOCX) toplu şəkildə emal edən, namizəd məlumatlarını
(ad, təcrübə, bacarıqlar, iş üstünlüyü) çıxarıb PostgreSQL-də saxlayan bir sistem.
 
## Arxitektura
 
```
POST /cv/upload (zip)
   → zip açılır (Zip Slip qorunması ilə)
   → BatchJobRun (PENDING) yaradılır
   → Spring Batch job asinxron başladılır (JobOperator)
   → dərhal jobId qaytarılır (202 Accepted)
 
Spring Batch (Reader → Processor → Writer, chunk=10):
   CvFileItemReader   → qovluqdakı fayl path-larını oxuyur
   CvFileItemProcessor → Apache Tika ilə mətn çıxarır, CandidateInfoExtractor
                          ilə strukturlaşdırır
   CandidateItemWriter → Candidate-ları DB-yə yazır
 
   Korlanmış fayl → skip (CvSkipListener → SkippedFile cədvəli)
   Müvəqqəti I/O xətası → 3 dəfə retry
 
Job bitəndə: CvJobExecutionListener → BatchJobRun yenilənir, email göndərilir
 
GET /jobs/{id}         → status, statistika
GET /candidates        → filter (skill, minExperience, location, jobType)
GET /candidates/export/csv | /excel → yükləmə
```
 
## Data çıxarma - məhdudiyyətlər
 
Mətndən struktur çıxarma **heuristik/regex əsaslıdır**, ML/NLP deyil - 100% dəqiqlik
hədəf deyil:
- Ad: CV-nin ilk sətirlərindən, "Böyük hərflə başlayan söz" pattern-i ilə tapılır;
  BÜTÜN BÖYÜK HƏRFLƏ yazılan adları tapmır
- Təcrübə ili: "X years/yrs" pattern-i; tarix aralığından (2019-2024) hesablamır
- Bacarıqlar/lokasiya/iş tipi: əvvəlcədən müəyyən edilmiş, `application.yaml`-da
  konfiqurasiya olunan siyahılarla uyğunlaşdırılır (`cv-extraction.*`)
## Təhlükəsizlik
 
- JWT (bu tətbiqin öz daxilində, `JwtAuthenticationFilter`)
- ROLE_USER / ROLE_ADMIN - admin yeni istifadəçi yarada bilər (`POST /admin/users`),
  ilk admin `DataInitializer` ilə avtomatik yaranır
- Zip Slip qorunması (`resolveSafely` - fayl adında `../` olsa rədd edilir)
## Tech stack
 
Java 21, Spring Boot 4.1, Spring Batch 6, Apache Tika, Apache POI, PostgreSQL,
Flyway, Spring Mail, springdoc-openapi, MapStruct, JUnit 5 + Mockito.
 
## Running
 
```bash
cp .env.example .env
docker compose up --build
```
 
Lazımi `.env` dəyərləri: `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `ADMIN_USERNAME`,
`ADMIN_EMAIL`, `ADMIN_PASSWORD`, `MAIL_USERNAME`, `MAIL_PASSWORD` (Gmail App Password).
 
## Testing
 
```bash
mvn clean test
```
 
## Production considerations
 
- Extraction qaydaları regex-əsaslıdır - real dəqiqlik üçün NLP/ML lazımdır
- Fayllar local diskdə saxlanır - miqyaslana bilən deploy üçün S3/object storage lazımdır
- `SimpleAsyncTaskExecutor` limitsizdir - yüksək yükdə `ThreadPoolTaskExecutor`
  (limitli) istifadə olunmalıdır
