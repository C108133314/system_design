from django.shortcuts import render, redirect
from django.views.decorators.csrf import csrf_exempt
from .models import *
from django.utils import timezone
import os
from django.conf import settings
from django.http import JsonResponse
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from Api.views import sendNotifyToDevice

# Create your views here.

@login_required(login_url="/log-in/")
def deleteComment(request, Review_pk,pk):
    if request.method == "POST":
        comment = Comment.objects.get(pk = pk)
        comment.delete()
    return redirect("/reviewDetail/" + str(Review_pk))

@login_required(login_url="/log-in/")
def updateComment(request, Review_pk, pk):
    comment = Comment.objects.get(pk=pk)
    review = Review.objects.get(pk = Review_pk)
    if request.method == "POST":
        comment.content = request.POST.get("content")
        comment.save()
        return redirect("/reviewDetail/" + str(Review_pk))
    return render(request, "update/updateComment.html", locals())

@login_required(login_url="/log-in/")
def deleteReview(request, pk):
    review = Review.objects.get(pk = pk)
    review.delete()
    return redirect("/review-List/")

@login_required(login_url="/log-in/")
def updateReview(request, pk):
    if request.method == "POST":
        title = request.POST.get("title")
        ISBN = request.POST.get("ISBNcode")
        publisher = request.POST.get("publisher")
        author = request.POST.get("author")
        category = Category.objects.get(pk=request.POST.get("category"))

        review = Review.objects.get(pk = pk)
        try:
            img = request.FILES["img_file"]
            file_name_suffix = img.name.split(".")[-1]
            if file_name_suffix in ["jpg", "png", "gif", "jpeg","jfif" ]:
                review.img = img
        except:
            pass
        created_by = User.objects.get(username=request.user)

        review.title = title
        review.created_by = created_by
        review.publisher = publisher
        review.category = category
        review.ISBN = ISBN
        review.author = author
        review.content = request.POST.get("content")
        review.rating = request.POST.get("rating")
        review.save()
    review = Review.objects.get(pk = pk)
    categorylist = Category.objects.all()
    return render(request, "update/updateReview.html", locals())

def register(request):
    context = {}
    if request.method == "POST":
        username = request.POST.get("username")
        password = request.POST.get("password")
        c_password = request.POST.get("c_password")
        email = request.POST.get("email")
        if c_password != password:
            context["wrong"] = "密碼輸入不一致!!"
            return render(request, "index/register.html", context)
        elif '@nkust.edu.tw' not in email:
            context["wrong"] = "請輸入高科大信箱"
            return render(request, "index/register.html", context)
        elif User.objects.filter(email=email).exists():
            
            context["wrong"] = "用戶信箱已存在"
            return render(request, "index/register.html", context)
        elif User.objects.filter(username=username).exists():
            
            context["wrong"] = "用戶名已存在"
            return render(request, "index/register.html", context)      
        else:
            user = User.objects.create_user(username, email, password)
            user.save()
            user = authenticate(request,username = username, password=password)
            if user is not None:
                login(request, user)
                return redirect("/")
            else :
                
                context["wrong"] = "請回報"
                return render(request, "index/register.html", context)
    return render(request, "index/register.html")


@login_required(login_url="/log-in/")
def log_out(request):
    logout(request)
    return redirect("/")

def log_in(request):
    if request.method == "POST":
        username = request.POST.get("username")
        password = request.POST.get("password")
        user = authenticate(request,username = username, password=password)
        if user is not None:
            login(request, user)
            return redirect("/")
        else :
            context = {}
            context["wrong"] = "帳號密碼輸入錯誤!!"
            return render(request, "index/login.html", context)
    return render(request, "index/login.html")
@login_required(login_url="/log-in/")
def addComment(request, pk):
    if request.method == "POST":
        review = Review.objects.get(pk=pk)
        created_by = User.objects.get(username=request.user)
        content = request.POST.get("content")
        data = Comment(content = content, created_by = created_by, review = review)
        data.save()
        if(created_by != review.created_by):
            
            sendNotifyToDevice(review.created_by,"comment_notify", message_from = created_by, content = data.shortContent())
        
    return redirect("/reviewDetail/" + str(pk))

def reviewDetail(request, pk):
    review = Review.objects.get(pk=pk)
    review.counter += 1
    review.save()
    comments = Comment.objects.filter(review = review)
    return render(request, "show/reviewDetail.html", locals())


def reviewList(request):
    review = Review.objects.all().order_by('-created_at')
    return render(request, "show/reviewList.html", locals())

@login_required(login_url="/log-in/")
def addCategory(request):
    if request.method == "POST":
        title = request.POST.get("title")
        if title != "":
            Category(title=title).save()
            print("saved")
    categorylist = Category.objects.all()
    return render(request, "edit/addCategory.html" , locals())

@login_required(login_url="/log-in/")
def addReview(request):

    if request.method == "POST":
        title = request.POST.get("title")
        ISBN = request.POST.get("ISBNcode")
        publisher = request.POST.get("publisher")
        author = request.POST.get("author")
        category = Category.objects.get(pk=request.POST.get("category"))
        img = request.FILES["img_file"]

        created_by = User.objects.get(username=request.user)

        file_name_suffix = img.name.split(".")[-1]
        if file_name_suffix in ["jpg", "png", "gif", "jpeg","jfif" ]:
            Review(title=title, created_by=created_by, publisher=publisher, category=category,ISBN = ISBN,
                   img=img, author=author, content=request.POST.get("content"), rating=request.POST.get("rating")).save()
    categorylist = Category.objects.all()
    return render(request, "edit/addReview.html", locals())


def index(request):
    review = Review.objects.all()
    latest_review = review.order_by('-created_at')[0:5]
    hot_review = review.order_by('-counter')[0:5]

    return render(request, "index/index.html", locals())


@csrf_exempt
def upload_image(request):  # for tinymce editor
    if request.method == "POST" and request.user.is_authenticated:
        file_obj = request.FILES['file']
        file_name_suffix = file_obj.name.split(".")[-1]
        if file_name_suffix not in  ["jpg", "png", "gif", "jpeg","JPG", "PNG", "GIF", "JPEG","jfif" ,"JFIF"  ]:
            return JsonResponse({"message": "Wrong file format"})

        upload_time = timezone.now()
        path = os.path.join(
            settings.MEDIA_ROOT,
            'tinymce',
            str(upload_time.year),
            str(upload_time.month),
            str(upload_time.day)
        )
        # If there is no such path, create
        if not os.path.exists(path):
            os.makedirs(path)

        file_path = os.path.join(path, file_obj.name)

        file_url = f'{settings.MEDIA_URL}tinymce/{upload_time.year}/{upload_time.month}/{upload_time.day}/{file_obj.name}'
        print(file_url)
        if os.path.exists(file_path):
            return JsonResponse({
                "message": "file already exist",
                'location': file_url
            })

        with open(file_path, 'wb+') as f:
            for chunk in file_obj.chunks():
                f.write(chunk)

        return JsonResponse({
            'message': 'Image uploaded successfully',
            'location': file_url
        })
    return JsonResponse({'detail': "Wrong request"})
