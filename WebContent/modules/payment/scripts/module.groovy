import org.metamorphosis.core.ActionSupport 
import static groovy.json.JsonOutput.toJson as json

class Gender {
  def type
  def label
  def categories = []
}

class Category {
  def name
  def selected
  def products = []
}

class Product {
  def name
  def price
  def image
}

class ModuleAction extends ActionSupport {

    def genders = [] 
    
    def String execute(){
       loadProducts()
       SUCCESS
    }
    
    def search() {
       def query = getParameter("query")
       response.writer.write(json([status: 0]))
    }
    
    def order() {
       response.writer.write(json([status: 1]))
    }
    
    def loadProducts() {
       def gender = new Gender(type : "female",label : "femme")
       def category = new Category(name : "T-Shirts",selected : true)
       category.products << new Product(name : "T-Shirt",price : 15000,image:"s6.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"s7.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"s8.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"33.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"34.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"35.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"40.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"41.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"42.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"43.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"55.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"s2.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"s3.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"s4.jpg")
       category.products << new Product(name : "T-Shirt",price : 15000,image:"s5.jpg")
       gender.categories << category
       category = new Category(name : "Jupes")
       category.products << new Product(name : "Jupe",price : 15000,image:"8.jpg")
       category.products << new Product(name : "Jupe",price : 15000,image:"9.jpg")
       category.products << new Product(name : "Jupe",price : 15000,image:"10.jpg")
       gender.categories << category
       category = new Category(name : "Montres")
       category.products << new Product(name : "Montre",price : 15000,image:"11.jpg")
       category.products << new Product(name : "Montre",price : 15000,image:"12.jpg")
       category.products << new Product(name : "Montre",price : 15000,image:"13.jpg")
       gender.categories << category
       category = new Category(name : "Sandales")
       category.products << new Product(name : "Sandale",price : 15000,image:"14.jpg")
       category.products << new Product(name : "Sandale",price : 15000,image:"15.jpg")
       category.products << new Product(name : "Sandale",price : 15000,image:"16.jpg")
       gender.categories << category
       category = new Category(name : "Bijoux")
       category.products << new Product(name : "Bijou",price : 15000,image:"17.jpg")
       category.products << new Product(name : "Bijou",price : 15000,image:"18.jpg")
       category.products << new Product(name : "Bijou",price : 15000,image:"19.jpg")
       gender.categories << category
       genders << gender
       gender = new Gender(type : "male",label : "homme")
       category = new Category(name : "Montres",selected : true)
       category.products << new Product(name : "Montre",price : 15000,image:"p4.jpg")
       category.products << new Product(name : "Montre",price : 15000,image:"p5.jpg")
       category.products << new Product(name : "Montre",price : 15000,image:"p6.jpg")
       gender.categories << category
       genders << gender
       gender = new Gender(type : "child",label : "enfant")
       category = new Category(name : "Robes",selected : true)
       category.products << new Product(name : "Robe",price : 15000,image:"47.jpg")
       category.products << new Product(name : "Robe",price : 15000,image:"56.jpg")
       category.products << new Product(name : "Robe",price : 15000,image:"57.jpg")
       gender.categories << category
       genders << gender
     
    }
    
}

new ModuleAction()