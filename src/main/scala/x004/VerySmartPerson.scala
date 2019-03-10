package x004

class VerySmartPerson(private // don't forget to make it private, because it will be accessible via "p._name=...."
                      var _name: // actually we have field "_name"
                      String) {
  def name = s"<${_name}>" // custom getter
  def name_= (aName: String) = { _name = aName.toUpperCase } // custom setter "name_=" to be used as "p.name = ..."
}
