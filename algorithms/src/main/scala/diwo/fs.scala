package diwo

import ExtendedSyntax.ExSyntax

object fs {
  
  /** obtain file from resources folder */
  def obtainResource(fileName: String) =
    Option(getClass.getClassLoader.getResource(fileName))
      .map(_.getFile)
      
  /** obtain file, or die with meaningful message */
  def resourceOrDie(fileName: String) =
    obtainResource(fileName)
      .getOrElse(!msg.file_not_found(fileName))
      
}
