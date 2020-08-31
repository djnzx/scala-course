package request.dto.prod_comp.rs

import java.util.Date

case class MetaData(
  id: Long, 
  creationDate: Date = new Date(),
  lastUpdatedDate: Date = new Date()
) 
