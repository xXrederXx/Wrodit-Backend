# Swaggwe Page Thread Schema

- Exported: 18.3.2026, 08:11:19
- Source: https://chatgpt.com/c/69ba4a85-4f14-832d-b60d-e6e668bf011d
- Messages: 4

**You:**

How can i tell the scheme that i return a Page<Thread> so it dosnt just show the generic page implementation in the swagger?

@ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Page generated",
            content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The user was unauthorized",
            content = @Content)
      })

* * *

**ChatGPT:**

Swagger (via Springdoc OpenAPI / Swagger) doesn’t understand Java generics like `Page<Thread>` out of the box—so if you just give it `Page.class`, it falls back to a generic structure.

To fix this, you have a few solid options:

---

## ✅ **Option 1: Create a wrapper DTO (recommended)**

Define a concrete class that represents your paged response:

Java@Schema(name = "ThreadPage")  
public class ThreadPage extends PageImpl<Thread> {  
  
    public ThreadPage(  
        @JsonProperty("content") List<Thread> content,  
        @JsonProperty("pageable") Pageable pageable,  
        @JsonProperty("totalElements") long total  
    ) {  
        super(content, pageable, total);  
    }  
}

Then update your annotation:

Java@ApiResponse(  
    responseCode = "200",  
    description = "Page generated",  
    content = @Content(schema = @Schema(implementation = ThreadPage.class))  
)

👉 This works because Swagger now sees a **concrete type**, not a generic.

---

## ✅ **Option 2: Use `@ArraySchema` + manually describe pagination**

If you don’t want a wrapper class:

Java@ApiResponse(  
    responseCode = "200",  
    description = "Page generated",  
    content = @Content(  
        schema = @Schema(implementation = ThreadPageResponse.class)  
    )  
)

Where `ThreadPageResponse` is a simple DTO like:

Javapublic class ThreadPageResponse {  
    public List<Thread> content;  
    public int page;  
    public int size;  
    public long totalElements;  
}

---

## ⚠️ **Why your current code doesn’t work**

Java@Schema(implementation = Page.class)

This tells Swagger:

> “The response is a Page”

But it has **no idea what `T` is**, so it shows a generic page model instead of `Thread`.

---

## 💡 Pro tip (cleanest approach)

If you're using Spring Boot with Springdoc:

* Return `Page<Thread>` in your controller
* But **document with a DTO (ThreadPageResponse)**

This keeps:

* your API clean
* your docs accurate
* your code maintainable

---

If you want, I can generate a **perfect Swagger schema that matches Spring’s Page JSON exactly** (including `sort`, `pageable`, etc.).

* * *

**You:**

But i dont want to make a new class with the same stuff every time, could i just create a base PagableDTO with the non generic stuff, like pageable, sorting, total etc. and then just create ThreadPagableDTO extends PagableDTO.

* * *

**ChatGPT:**

Yes — that’s actually a **very solid approach**, and it’s commonly used to avoid repeating yourself 👍

But there’s one important detail: **your base class cannot hold the generic `content` field in a way Swagger understands properly**. You still need to define the concrete `content` type in each subclass.

---

## ✅ Recommended structure

### 1. Base DTO (non-generic metadata only)

Javapublic class PageableDTO {  
  
    public int page;  
    public int size;  
    public long totalElements;  
    public int totalPages;  
    public boolean first;  
    public boolean last;  
}

---

### 2. Concrete DTO per type

Javapublic class ThreadPageDTO extends PageableDTO {  
  
    public List<Thread> content;  
}

---

### 3. Swagger annotation

Java@ApiResponse(  
    responseCode = "200",  
    description = "Page generated",  
    content = @Content(schema = @Schema(implementation = ThreadPageDTO.class))  
)

---

## ⚠️ Why not a generic base like `PagableDTO<T>`?

You might try this:

Javapublic class PageableDTO<T> {  
    public List<T> content;  
    ...  
}

But Swagger (via Springdoc) will still **lose the type information for `T`**, and you’ll end up back at:

> `content: object[]`

So this doesn’t solve your original problem.

---

## 💡 Best balance (what you're proposing, refined)

✔ Reuse all pagination fields in a base class  
✔ Only redefine `content` per DTO

This keeps:

* minimal duplication
* correct Swagger docs
* simple code

---

## 🚀 Optional: make it even cleaner with mapping

You can create a utility:

Javapublic static <T, D extends PageableDTO> D map(Page<T> page, D dto) {  
    dto.page = page.getNumber();  
    dto.size = page.getSize();  
    dto.totalElements = page.getTotalElements();  
    dto.totalPages = page.getTotalPages();  
    dto.first = page.isFirst();  
    dto.last = page.isLast();  
    return dto;  
}

Then just set `content` separately.

---

## Bottom line

✔ Yes, your idea is correct  
❗ Just don’t make the base class generic for Swagger’s sake  
✅ Keep `content` defined in each subclass

---

If you want a more advanced trick, I can show you how to **make Springdoc automatically resolve `Page<T>` without DTOs at all** (using a custom model converter).