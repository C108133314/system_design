from django.db.models.query import QuerySet
from django.http.response import HttpResponse
from django.shortcuts import render
from mainapp.models import Review, Category, Comment, UserDeviceToken
from django.core import serializers
from django.contrib.auth import authenticate, login, logout
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth.models import User
from django.utils import timezone
import os
import json
from django.conf import settings
from mainapp.models import Comment
from django.core.files.uploadedfile import InMemoryUploadedFile
from django.contrib.auth.models import User, Group
import base64
import io
from PIL import Image
from django.contrib.auth.models import User, Group
from .serializers import UserSerializer, ReviewSerializer
from firebase_admin import messaging,credentials
import firebase_admin
from django.db.models import Q



cred = credentials.Certificate(os.path.join(settings.BASE_DIR, "android-book-sharing-system-firebase-adminsdk-za3dt-8b6c27129e.json"))
firebase_admin.initialize_app(cred)

def check_out_my_review(request):
    if request.method == "POST":
        if(checkAuthenticate(request) != True):
            return JsonResponse({"status_code":"0", "msg":"authentication fail"})
        pass

def checkAuthenticate(request):
    username = request.POST.get("username")
    password = request.POST.get("password")
    user = authenticate(request,username = username, password=password)
    print(username , password)
    if user is not None:
        login(request, user)
        #context['status_code'] = "1"
        #context['msg'] = "success"
        #context['username'] = user.username
        #context['email'] = user.email
        return True
    else :
        return False
        #context['status_code'] = "0"
        #context["msg"] = "帳號密碼輸入錯誤!!"
    return False


@csrf_exempt
def deleteComment(request):
    if request.method == "POST":
        if(checkAuthenticate(request) != True):
            return JsonResponse({"status_code":"0", "msg":"authentication fail"})
        try:
            comment = Comment.objects.get(pk = request.POST.get("pk"))
            comment.delete()
            context = default_msg(1)
        except Exception as e:
            context = default_msg(0)
            context["msg"] = str(e)
        return JsonResponse(context)

@csrf_exempt
def get_comment(request):
    if request.method == "POST":
        rpk = request.POST.get("rPk")
        comment = Comment.objects.filter(review = Review.objects.get(pk = rpk)).order_by('-created_at')
        data_json = serializers.serialize('json', comment, use_natural_foreign_keys=True, use_natural_primary_keys=True)
        return HttpResponse(json.dumps({"queryset":eval(data_json)}), content_type='application/json')

@csrf_exempt
def get_category(request):
    if request.method == "POST":
        category = Category.objects.all()
        data_json = serializers.serialize('json', category, use_natural_foreign_keys=True, use_natural_primary_keys=True)
        return HttpResponse(json.dumps({"queryset":eval(data_json)}), content_type='application/json')


# Create your views here.
@csrf_exempt
def get_review(request):
    if request.method == "POST":
        review_type = request.POST.get("type")
        review = Review.objects.select_related('created_by')
        if review_type == "latest_review":
            review = review.order_by('-created_at')
        elif review_type == "suggest_review":
            review = review.order_by('-counter')[0:7]
        elif review_type == "hot_review":
            review = review.order_by('-counter')
        elif review_type == "all":
            pass
        data_json = serializers.serialize('json', review, use_natural_foreign_keys=True, use_natural_primary_keys=True)
        #serialized = ReviewSerializer(review, many=True)
        #json_representation = JSONRenderer().render(serialized.data)
        #return HttpResponse(json_representation)
        return HttpResponse(json.dumps({"queryset":eval(data_json)}), content_type='application/json')
        
@csrf_exempt
def get_review_detail(request):
    if request.method == "POST":
        r_id = request.POST.get("r_id")
        review = Review.objects.filter(pk = r_id)
        data_json = serializers.serialize('json', review, use_natural_foreign_keys=True, use_natural_primary_keys=True)
        return HttpResponse(data_json, content_type='application/json')

@csrf_exempt
def log_in(request):
    if request.method == "POST":
        username = request.POST.get("username")
        password = request.POST.get("password")
        user = authenticate(request,username = username, password=password)
        context = {}
        context['status_code'] = "0"
        context["msg"] = "error"
        if user is not None:
            login(request, user)
            user_device_token_initialize(request)
            context['status_code'] = "1"
            context['msg'] = "success"
            context['username'] = user.username
            context['email'] = user.email
        else :
            context['status_code'] = "0"
            context["msg"] = "帳號密碼輸入錯誤!!"
        print(context)
    return JsonResponse(context)

@csrf_exempt
def register(request):
    context = {}
    if request.method == "POST":
        username = request.POST.get("username")
        password = request.POST.get("password")
        c_password = request.POST.get("c_password")
        email = request.POST.get("email")
        if c_password != password:
            context["msg"] = "密碼輸入不一致!!"
            context['status_code'] = "0"
            return render(request, "index/register.html", context)
        elif '@nkust.edu.tw' not in email:
            context["msg"] = "請輸入高科大信箱"
            context['status_code'] = "0"
            return render(request, "index/register.html", context)
        elif User.objects.filter(email=email).exists():
            context["msg"] = "用戶信箱已存在"
            context['status_code'] = "0"
            return render(request, "index/register.html", context)
        elif User.objects.filter(username=username).exists():
            context["msg"] = "用戶名已存在"
            context['status_code'] = "0"
            return render(request, "index/register.html", context)      
        else:
            user = User.objects.create_user(username, email, password)
            user.save()
            user = authenticate(request,username = username, password=password)
            if user is not None:
                login(request, user)
                context["msg"] = "success"
                context['status_code'] = "1"
            else :
                context["msg"] = "請回報"
                context['status_code'] = "0"
    return JsonResponse(context)

@csrf_exempt
def addComment(request):
    if request.method == "POST":
        if(checkAuthenticate(request) != True):
            return JsonResponse({"status_code":"0", "msg":"authentication fail"})
        context = {}
        try:
            review = Review.objects.get(pk=request.POST.get("rpk"))
            created_by = User.objects.get(email = request.POST.get("created_by"))
            content = request.POST.get("content")
            data = Comment(content = content, created_by = created_by, review = review)
            data.save()
            context = default_msg(1)
            context["cid"] = data.pk
            if(created_by != review.created_by):
                sendNotifyToDevice(review.created_by,"comment_notify", message_from = created_by, content = data.shortContent())
        except Exception as e:
            context = default_msg(0)
            context['msg'] = str(e)
        return HttpResponse(json.dumps(context), content_type='application/json')

def sendNotifyToDevice(user, type, **kwargs):
    if type == "comment_notify":
        # This registration token comes from the client FCM SDKs.
        user_devic_list = list(UserDeviceToken.objects.filter(user = user).filter(~Q(token = "")).values_list("token",flat=True))
        if len(user_devic_list) != 0:
            registration_token = user_devic_list
            # See documentation on defining a message payload.
            message = messaging.MulticastMessage(
                    notification=messaging.Notification(
                    title='你的書籍分享有一個新的回覆',
                    body= kwargs["message_from"].username + "說: " + kwargs["content"],
                    ),
                    tokens=registration_token,
            )
            # Send a message to the device corresponding to the provided
            # registration token.
            response = messaging.send_multicast(message)
            # Response is a message ID string.
            print('Successfully sent message:', response)

@csrf_exempt
def updateComment(request):
    if request.method == "POST":
        if(checkAuthenticate(request) != True):
            return JsonResponse({"status_code":"0", "msg":"authentication fail"})
        context = {}
        try:
            comment = Comment.objects.get(pk=request.POST.get("cpk"))
            #review = Review.objects.get(pk = request.POST.get("rpk"))
            comment.content = request.POST.get("content")
            comment.save()
            context = default_msg(1)
        except Exception as e:
            context = default_msg(0)
            context['msg'] = str(e)
        return JsonResponse(context)
        
        

def default_msg(status):
    context = {}
    if status == 1:
        context['msg'] = "success"
        context['status_code'] = "1"
    else:
        context['msg'] = "error"
        context['status_code'] = "0" 
    return context

@csrf_exempt
def deleteReview(request):
    if request.method == "POST":
        if(checkAuthenticate(request) != True):
            return JsonResponse({"status_code":"0", "msg":"authentication fail"})
        try:
            review = Review.objects.get(pk = request.POST.get("pk"))
            review.delete()
            context = default_msg(1)
        except Exception as e:
            context = default_msg(0)
            context["msg"] = str(e)
        return JsonResponse(context)

@csrf_exempt
def updateReview(request):
    if request.method == "POST":
        if(checkAuthenticate(request) != True):
            return JsonResponse({"status_code":"0", "msg":"authentication fail"})
        context = {}
        try:
            pk = request.POST.get("pk")
            title = request.POST.get("title")
            ISBN = request.POST.get("ISBNcode")
            publisher = request.POST.get("publisher")
            author = request.POST.get("author")
            category = Category.objects.get(title=request.POST.get("category"))
            img = request.POST["img_file"]
            img = decodeDesignImage(img)
            img_io = io.BytesIO()
            img.save(img_io, format='JPEG')
            img = InMemoryUploadedFile(img_io, field_name=None, name="test"+".jpg", content_type='image/jpeg', size=img_io.tell, charset=None)

            file_name_suffix = img.name.split(".")[-1]
            if file_name_suffix in ["jpg", "png", "gif", "jpeg","JPG", "PNG", "GIF", "JPEG" ]:
                review = Review.objects.get(pk = pk)
                review.title = title
                review.publisher = publisher
                review.category = category
                review.ISBN = ISBN
                review.img = img
                review.author = author
                review.content =request.POST.get("content")
                review.rating = request.POST.get("rating")
                review.save()
            context = default_msg(1)
        except Exception as e:
            context = default_msg(2)
            context['msg'] = str(e)
        return HttpResponse(json.dumps(context), content_type='application/json')


@csrf_exempt
def upload_image(request):  # for tinymce editor
    if request.method == "POST":
        if(checkAuthenticate(request) != True):
            return JsonResponse({"status_code":"0", "msg":"authentication fail"})
        img = request.POST["img_file"]
        img = decodeDesignImage(img)
        img_io = io.BytesIO()
        img.save(img_io, format='JPEG')
        auto_filename = str(timezone.localtime().timestamp())
        img = InMemoryUploadedFile(img_io, field_name=None, name=auto_filename+".jpg", content_type='image/jpeg', size=img_io.tell, charset=None)
        file_name_suffix = img.name.split(".")[-1]
        if file_name_suffix not in  ["jpg", "png", "gif", "jpeg","JPG", "PNG", "GIF", "JPEG" ]:
            return JsonResponse({"message": "Wrong file format"})

        upload_time = timezone.now()
        path = os.path.join(
            settings.MEDIA_ROOT,
            'tinymce',
            str(upload_time.year),
            str(upload_time.month),
            str(upload_time.day),
            auto_filename,
        )
        # If there is no such path, create
        if not os.path.exists(path):
            os.makedirs(path)

        file_path = os.path.join(path, img.name)

        file_url = f'{settings.MEDIA_URL}tinymce/{upload_time.year}/{upload_time.month}/{upload_time.day}/{auto_filename}/{img.name}'
        print(file_url)
        if os.path.exists(file_path):
            return JsonResponse({
                "msg": "file already exist",
                'location': file_url[1:],
                'status_code' : "1"
            })

        with open(file_path, 'wb+') as f:
            for chunk in img.chunks():
                f.write(chunk)
        return JsonResponse({
            'msg': 'Image uploaded successfully',
            'location': file_url[1:],
            'status_code' : "1"
        })
    return JsonResponse({'msg': "Wrong request",'status_code' : "0" })



def addCategory(request):
    if request.method == "POST":
        if(checkAuthenticate(request) != True):
            return JsonResponse({"status_code":"0", "msg":"authentication fail"})
        context = {}
        try:
            title = request.POST.get("title")
            if title != "":
                Category(title=title).save()
                print("saved")
            context = default_msg(2)
        except Exception as e:
            context = default_msg(1)
    #categorylist = Category.objects.all()
    #return render(request, "edit/addCategory.html" , locals())
    return JsonResponse(context)

@csrf_exempt
def addReview(request):
    context = {}
    if request.method == "POST":
        if(checkAuthenticate(request) != True):
            return JsonResponse({"status_code":"0", "msg":"authentication fail"})
        try:
            title = request.POST.get("title")
            ISBN = request.POST.get("ISBNcode")
            publisher = request.POST.get("publisher")
            author = request.POST.get("author")
            category = Category.objects.filter(title = request.POST.get("category"))[0]
            content = content=request.POST.get("content")
            rating=request.POST.get("rating")
            img = request.POST["img_file"]
            img = decodeDesignImage(img)
            img_io = io.BytesIO()
            img.save(img_io, format='JPEG')
            img = InMemoryUploadedFile(img_io, field_name=None, name="test"+".jpg", content_type='image/jpeg', size=img_io.tell, charset=None)
            #created_by = User.objects.get(username=request.POST.get("uid"))
            created_by = User.objects.get(email = request.POST.get("user"))
            file_name_suffix = img.name.split(".")[-1]
            if file_name_suffix in  ["jpg", "png", "gif", "jpeg","JPG", "PNG", "GIF", "JPEG" ]:
                Review(title=title
                , created_by=created_by
                , publisher=publisher
                , category=category
                , ISBN = ISBN
                , img=img
                , author=author
                , content = content
                , rating = rating).save()
            context = default_msg(1)
        except Exception as e:
            context = default_msg(0)
            context['msg'] = str(e)
        
        print(context)
        return HttpResponse(json.dumps(context), content_type='application/json')

def user_device_token_initialize(request):
    try:
        token = request.POST.get("device_token")
        user = User.objects.get(username = request.POST.get("username"))
        if len(UserDeviceToken.objects.filter(token = token)) == 0: 
            UserDeviceToken(user = user, token = token).save()
    except Exception as e:
        return str(e)
    return True


def decodeDesignImage(data):
    try:
        data = base64.b64decode(data.encode('UTF-8'))
        buf = io.BytesIO(data)
        img = Image.open(buf)
        return img
    except:
        return None