package c07x02_FileSync

object CopyFolderApp extends App {
  
  val src = os.pwd / "1"
  val dst = os.pwd / "2"

  os.remove.all(dst)
  
  os.makeDir.all(src)
  os.write.over(src / "a", "content#A")
  os.write.over(src / "b", "content#B")
  
  os.copy.over(src, dst, createFolders = true) 
}
