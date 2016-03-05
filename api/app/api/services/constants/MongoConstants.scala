package api.services.constants

import com.google.inject.Singleton

@Singleton
class MongoConstants {
  val applicationDatabaseName = "arpa(n)2et"

  val documentsCollection = "documents"
  val pdfCollectionName = "pdfs"
  val writeUpsCollectionName = "write_ups"
  val publicImagesCollectionName = "public_images"
  val photographyCollectionName = "photography"
}
