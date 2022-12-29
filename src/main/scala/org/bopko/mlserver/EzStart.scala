package org.bopko.mlserver

import org.bopko.mlserver.bm.{Application, CalculateAttributes}
import zio.http._
import zio.http.Server
import zio.http.ServerConfig.LeakDetectionLevel
import zio.http.model.{Method, Status}
import zio.{Console, Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

import scala.util.Try

object EzStart extends ZIOAppDefault {

  val port: Int = 8080

  val invocations: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    case req @ Method.POST -> !! / "invocations" =>

      val appZio = req.body.asString.map(json =>
        try Right(Application.fromJson(json))
        catch {
          case e: Throwable => Left(e)
        })

      for {
        applicationOrError <- appZio // applicationOrError is a ZIO[Any, Throwable, Either[Throwable, Application]]
        response <- applicationOrError match {
          case Right(application) =>
            ZIO.succeed(Response.json(CalculateAttributes.calc(application).toJson)) // toJson is from zio-json
          case Left(error) =>
            ZIO.debug(s"Failed to parse input: $error")
            .as(Response.text("Failed to parse input").setStatus(Status.BadRequest))
        }
      } yield response
  }

  val ping: Http[Any, Nothing, Request, Response] = Http.collect[Request] {
    case Method.GET -> !! / "ping" => Response.text("pong").setStatus(Status.Ok)
  }

  val app: Http[Any, Throwable, Request, Response] = invocations ++ ping

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ZIOAppArgs.getArgs.flatMap { args =>

    val nThreads: Int = args.headOption.flatMap(x => Try(x.toInt).toOption).getOrElse(0)

    val config = ServerConfig.default
      .port(port)
      .leakDetection(LeakDetectionLevel.PARANOID)
      .maxThreads(nThreads)

    val configLayer = ServerConfig.live(config)

    // Install the app, ZIO.never = while(true),
    // *> = andThen,
    // provide a layer to the config
    (Server.install(app).flatMap { port =>
      Console.printLine(s"Server started on port $port")
    } *> ZIO.never).provide(configLayer, Server.live)

  }

}
