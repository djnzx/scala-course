package c07x02_FileSync

import os.{Path, SubPath}

object FileSyncApp extends App {
  /**
    * @param src - Absolute source path
    * @param dst - Absolute destination path
    */
  def sync(src: os.Path, dst: os.Path) = {
    for (srcAbsPath <- os.walk(src)) {
      
      // extract source sub-path
      val srcSubPath: SubPath = srcAbsPath.subRelativeTo(src)
      // combine destination path
      val dstAbsPath: Path    = dst / srcSubPath
      
      (os.isDir(srcAbsPath), os.isDir(dstAbsPath)) match {
        // one of them is folder - just copy over
        case (false, true) | 
             (true, false) => os.copy.over(srcAbsPath, dstAbsPath, createFolders = true)
        
        // both of them are files  
        case (false, false)
          if !os.exists(dstAbsPath)
            || !os.read.bytes(srcAbsPath).sameElements(os.read.bytes(dstAbsPath)) =>

          os.copy.over(srcAbsPath, dstAbsPath, createFolders = true)

        case _ => // do nothing
      }
    }
  }
  
}