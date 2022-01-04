from django.db import models
from django.contrib.auth.models import User
from django.db.models.base import Model
from tinymce import models as tinymce_models
from bs4 import BeautifulSoup
# Create your models here.




class Category(models.Model):
    title = models.CharField(verbose_name="title", max_length=255)
    def __str__(self):
        return self.title
    
    def natural_key(self):
        return (self.id, self.title)

     
class Review(models.Model):
    img = models.ImageField(upload_to='BookCover/%Y/%m/%d/', blank=True)
    title = models.CharField(verbose_name="title", max_length=255)
    ISBN = models.CharField(verbose_name="ISBN", max_length=255)
    author = models.CharField(max_length=255, default = "")
    publisher = models.CharField(verbose_name="publisher", max_length=255)
    category = models.ForeignKey(Category, verbose_name="category", on_delete=models.CASCADE)
    rating = models.IntegerField(verbose_name="評分")
    content = tinymce_models.HTMLField()
    created_at = models.DateTimeField("創建時間", auto_now_add=True)
    created_by = models.ForeignKey(User, verbose_name="created_by", on_delete=models.CASCADE)
    counter = models.IntegerField(default = 0)
    
    def __str__(self):
        return self.title
        
    def shortContent(self):
        return getContent(self.content)

    def makestar(self):
        temp = ""
        for i in range(self.rating):
            temp += str(i)
        return temp


class Comment(models.Model):
    content = tinymce_models.HTMLField()
    created_at =  models.DateTimeField(verbose_name="創建時間", auto_now_add=True)
    created_by = models.ForeignKey(User, verbose_name="創建使用者", on_delete=models.CASCADE)
    review = models.ForeignKey("Review", verbose_name="評論", on_delete=models.CASCADE)

    def __str__(self):
        return "Comment id_" + str(self.id)
        
    def shortContent(self):
        return getContent(self.content)

class UserDeviceToken(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    token = models.TextField()

class UserReceivedNotification(models.Model):
    content = models.TextField()
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    created_at =  models.DateTimeField(verbose_name="創建時間", auto_now_add=True)

class UserProfile(models.Model):
    user = models.OneToOneField(User, verbose_name="創建使用者", on_delete=models.CASCADE)
    img = models.ImageField(upload_to='UserProfile/%Y/%m/%d/', blank=True)

    def __str__(self):
        return self.user


# bs4 取文章特定內容
def getContent(content):
    data = ""
    soup = BeautifulSoup(content,features="html.parser")
    for index , item in enumerate(soup.get_text()):
        if index >= 50:
            break
        data += item
    return data